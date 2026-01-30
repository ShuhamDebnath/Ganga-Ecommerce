import mongoose from 'mongoose';

const vendorSchema = new mongoose.Schema({
  user_id: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true,
    unique: true
  },
  store_name: {
    type: String,
    required: true,
    unique: true,
    trim: true
  },
  store_description: String,
  store_logo: String, // URL from Cloudinary
  is_verified: {
    type: Boolean,
    default: false // Admin must approve
  },
  rating: {
    average: { type: Number, default: 0 },
    count: { type: Number, default: 0 }
  }
}, {
  timestamps: true
});

const Vendor = mongoose.model('Vendor', vendorSchema);
export default Vendor;