import jwt from 'jsonwebtoken';
import User from '../models/User.js';

// 1. Protect Routes (Verify Token)
export const protect = async (req, res, next) => {
  let token;

  if (
    req.headers.authorization &&
    req.headers.authorization.startsWith('Bearer')
  ) {
    try {
      token = req.headers.authorization.split(' ')[1];
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      req.user = await User.findById(decoded.id).select('-password');
      next();
    } catch (error) {
      console.error("Auth Error:", error.message);
      // Explicitly handle TokenExpiredError
      if (error.name === 'TokenExpiredError') {
        return res.status(401).json({ 
            success: false, // <--- IMPORTANT: Frontend expects this
            message: 'Session expired, please login again' 
        });
      }
      return res.status(401).json({ 
          success: false, 
          message: 'Not authorized, token failed' 
      });
    }
  } else {
    res.status(401).json({ 
        success: false, 
        message: 'Not authorized, no token' 
    });
  }
};

// 2. Authorize Roles (Check 'vendor' or 'admin')
export const authorize = (...roles) => {
  return (req, res, next) => {
    if (!req.user || !roles.includes(req.user.role)) {
      return res.status(403).json({ 
        message: `User role '${req.user?.role}' is not authorized to access this route` 
      });
    }
    next();
  };
};