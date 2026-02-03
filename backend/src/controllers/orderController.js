import Order from '../models/Order.js';

// @desc    Create new order (Handles Splitting)
// @route   POST /api/v1/orders
export const addOrderItems = async (req, res) => {
  try {
    const {
      orderItems, // Flat list from Client Cart
      shippingAddress,
      paymentMethod,
      totalPrice
    } = req.body;

    if (!orderItems || orderItems.length === 0) {
      return res.status(400).json({ message: 'No order items' });
    }

    // --- LOGIC: SPLIT ORDER BY VENDOR ---
    // 1. Group items by vendor_id
    const groupedItems = orderItems.reduce((acc, item) => {
      const vendorId = item.vendor_id;
      if (!acc[vendorId]) {
        acc[vendorId] = [];
      }
      acc[vendorId].push(item);
      return acc;
    }, {});

    // 2. Create Sub-Orders Array
    const subOrders = Object.keys(groupedItems).map((vendorId) => {
      const items = groupedItems[vendorId];
      // Calculate sub-total for this specific vendor
      const subTotal = items.reduce((sum, item) => sum + item.price * item.quantity, 0);
      
      return {
        vendor_id: vendorId,
        items: items.map(item => ({
          product_id: item.product_id,
          title: item.title,
          image: item.image,
          price: item.price,
          quantity: item.quantity
        })),
        sub_total: subTotal,
        shipping_status: 'Pending'
      };
    });

    // 3. Create Master Order
    const order = new Order({
      user_id: req.user._id,
      sub_orders: subOrders, // The split structure
      shipping_address: shippingAddress,
      payment_method: paymentMethod,
      total_price: totalPrice,
      is_paid: paymentMethod === 'COD' ? false : true, // Mock logic
      paid_at: paymentMethod === 'COD' ? null : Date.now()
    });

    const createdOrder = await order.save();

    res.status(201).json({
      success: true,
      data: createdOrder
    });

  } catch (error) {
    console.error("Order Create Error:", error);
    res.status(500).json({ message: 'Order creation failed', error: error.message });
  }
};

// @desc    Get logged in user orders
// @route   GET /api/v1/orders/myorders
export const getMyOrders = async (req, res) => {
  try {
    const orders = await Order.find({ user_id: req.user._id }).sort({ createdAt: -1 });
    res.json({ success: true, data: orders });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

// @desc    Cancel Order
// @route   PUT /api/v1/orders/:id/cancel
export const cancelOrder = async (req, res) => {
  try {
    const order = await Order.findById(req.params.id);

    if (order) {
      // Ensure user owns the order
      if (order.user_id.toString() !== req.user._id.toString()) {
        return res.status(401).json({ message: 'Not authorized to cancel this order' });
      }

      // Check if already delivered
      // (For simplicity, we check the first sub-order, or assume master status)
      const isDelivered = order.sub_orders.some(sub => sub.shipping_status === 'Delivered');
      
      if (isDelivered) {
        return res.status(400).json({ message: 'Cannot cancel a delivered order' });
      }

      // Mark all sub-orders as Cancelled
      order.sub_orders.forEach(sub => {
        sub.shipping_status = 'Cancelled';
      });

      const updatedOrder = await order.save();
      res.json({ success: true, data: updatedOrder });
    } else {
      res.status(404).json({ message: 'Order not found' });
    }
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};