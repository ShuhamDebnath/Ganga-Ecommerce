import mongoose from 'mongoose';

const orderSchema = new mongoose.Schema({
  user_id: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  // Shipping Details
  shipping_address: {
    fullName: { type: String, required: true },
    address: { type: String, required: true },
    city: { type: String, required: true },
    postalCode: { type: String, required: true },
    country: { type: String, required: true },
    phone: { type: String, required: true }
  },
  // Payment Details
  payment_method: { type: String, required: true }, // 'Card', 'COD'
  payment_result: {
    id: String,
    status: String,
    update_time: String,
    email_address: String
  },
  // Financials
  total_price: { type: Number, required: true, default: 0.0 },
  is_paid: { type: Boolean, required: true, default: false },
  paid_at: { type: Date },

  // --- THE CORE: SUB-ORDERS (Multi-Vendor Logic) ---
  sub_orders: [
    {
      vendor_id: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
      },
      items: [
        {
          product_id: {
            type: mongoose.Schema.Types.ObjectId,
            ref: 'Product',
            required: true
          },
          title: String,
          image: String,
          quantity: { type: Number, required: true },
          price: { type: Number, required: true } // Price at time of purchase
        }
      ],
      shipping_status: {
        type: String,
        enum: ['Pending', 'Processing', 'Shipped', 'Delivered', 'Cancelled'],
        default: 'Pending'
      },
      sub_total: { type: Number, required: true }
    }
  ]
}, {
  timestamps: true
});

const Order = mongoose.model('Order', orderSchema);
export default Order;