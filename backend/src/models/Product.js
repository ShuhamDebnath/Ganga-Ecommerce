import mongoose from 'mongoose';

const productSchema = new mongoose.Schema({
  vendor_id: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User', // Links to the Vendor (User)
    required: true
  },
  title: {
    type: String,
    required: true,
    trim: true,
    index: true // Helps with search
  },
  description: {
    type: String,
    required: true
  },
  category: {
    type: String,
    required: true, // e.g., "Electronics", "Fashion"
    index: true
  },
  price: {
    type: Number,
    required: true,
    min: 0
  },
  discount_price: {
    type: Number,
    default: 0
  },
  stock_quantity: {
    type: Number,
    required: true,
    min: 0
  },
  images: [{
    type: String // URLs from Cloudinary/S3
  }],
  rating: {
    average: { type: Number, default: 0 },
    count: { type: Number, default: 0 }
  },
  is_active: {
    type: Boolean,
    default: true
  }
}, {
  timestamps: true
});

// Create text index for search functionality
productSchema.index({ title: 'text', description: 'text', category: 'text' });

const Product = mongoose.model('Product', productSchema);
export default Product;