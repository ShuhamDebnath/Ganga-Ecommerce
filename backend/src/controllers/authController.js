import User from '../models/User.js';
import Vendor from '../models/Vendor.js';
import jwt from 'jsonwebtoken';

// Generate Access Token (15 mins)
const generateAccessToken = (id) => {
  return jwt.sign({ id }, process.env.JWT_SECRET, { expiresIn: '15m' });
};

// Generate Refresh Token (7 days)
const generateRefreshToken = (id) => {
  return jwt.sign({ id }, process.env.JWT_SECRET, { expiresIn: '7d' });
};

// @desc    Register a new user
// @route   POST /api/v1/auth/register
export const registerUser = async (req, res) => {
  try {
    const { name, email, password, role, store_name } = req.body;

    // 1. Check if user exists
    const userExists = await User.findOne({ email });
    if (userExists) {
      return res.status(400).json({ message: 'User already exists' });
    }

    // 2. Create User
    const user = await User.create({
      name,
      email,
      password,
      role: role || 'customer'
    });

    // 3. If Vendor, create Vendor profile
    if (role === 'vendor') {
      if (!store_name) {
        return res.status(400).json({ message: 'Store name is required for vendors' });
      }
      await Vendor.create({
        user_id: user._id,
        store_name
      });
    }

    // 4. Generate Tokens
    const accessToken = generateAccessToken(user._id);
    const refreshToken = generateRefreshToken(user._id);

    // Save refresh token to DB
    user.refresh_token = refreshToken;
    await user.save();

    res.status(201).json({
      success: true,
      data: {
        _id: user._id,
        name: user.name,
        email: user.email,
        role: user.role,
        accessToken,
        refreshToken
      }
    });

  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

// @desc    Login user
// @route   POST /api/v1/auth/login
export const loginUser = async (req, res) => {
  try {
    const { email, password } = req.body;

    // 1. Find user (select password explicitly)
    const user = await User.findOne({ email }).select('+password');
    
    // 2. Check password
    if (user && (await user.matchPassword(password))) {
      const accessToken = generateAccessToken(user._id);
      const refreshToken = generateRefreshToken(user._id);

      // Rotate Refresh Token
      user.refresh_token = refreshToken;
      await user.save();

      res.json({
        success: true,
        data: {
          _id: user._id,
          name: user.name,
          email: user.email,
          role: user.role,
          accessToken,
          refreshToken
        }
      });
    } else {
      res.status(401).json({ message: 'Invalid email or password' });
    }
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

// @desc    Refresh Access Token
// @route   POST /api/v1/auth/refresh
export const refreshAccessToken = async (req, res) => {
  const { refreshToken } = req.body;

  if (!refreshToken) {
    return res.status(401).json({ success: false, message: 'No refresh token provided' });
  }

  try {
    // 1. Verify Refresh Token
    const decoded = jwt.verify(refreshToken, process.env.JWT_SECRET);

    // 2. Check if user exists and token matches DB
    const user = await User.findById(decoded.id).select('+refresh_token');
    
    if (!user || user.refresh_token !== refreshToken) {
      return res.status(403).json({ success: false, message: 'Invalid refresh token' });
    }

    // 3. Generate NEW Access Token
    const accessToken = generateAccessToken(user._id);
    
    // Optional: Rotate Refresh Token here too for extra security
    
    res.json({
      success: true,
      accessToken
    });

  } catch (error) {
    return res.status(403).json({ success: false, message: 'Invalid refresh token' });
  }
};