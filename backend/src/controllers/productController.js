import Product from '../models/Product.js';

// @desc    Get all products (Feed) with Search & Filter
// @route   GET /api/v1/products
export const getProducts = async (req, res) => {
  try {
    const { keyword, category, page = 1, limit = 10 } = req.query;
    
    // Build Query
    let query = { is_active: true };

    // Search Logic (Regex for partial matching)
    if (keyword) {
      query.$or = [
        { title: { $regex: keyword, $options: 'i' } },
        { description: { $regex: keyword, $options: 'i' } }
      ];
    }

    // Filter by Category
    if (category) {
      query.category = category;
    }

    // Pagination Logic
    const count = await Product.countDocuments(query);
    const products = await Product.find(query)
      .limit(limit * 1)
      .skip((page - 1) * limit)
      .sort({ createdAt: -1 }); // Newest products first

    res.json({
      success: true,
      data: products,
      pagination: {
        total: count,
        page: Number(page),
        pages: Math.ceil(count / limit)
      }
    });

  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

// @desc    Create a Product (Vendor Only)
// @route   POST /api/v1/products
export const createProduct = async (req, res) => {
  try {
    // Note: In production, you would verify req.user.role === 'vendor' here
    const { vendor_id, title, description, price, category, stock_quantity, images } = req.body;

    const product = await Product.create({
      vendor_id: req.user._id, 
      title,
      description,
      price,
      category,
      stock_quantity,
      images: images || ["https://placehold.co/600x400/orange/white?text=Ganga+Product"]
    });

    res.status(201).json({
      success: true,
      data: product
    });

  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};