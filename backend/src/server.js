import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import mongoose from 'mongoose';
import authRoutes from './routes/authRoutes.js';

// Initialize Environment Variables
dotenv.config();

const app = express();
const PORT = process.env.PORT || 8000;

// Middleware
app.use(cors()); // Allow Cross-Origin requests (crucial for KMP Web)
app.use(express.json()); // Parse JSON bodies

// Routes
app.use('/api/v1/auth', authRoutes); 

// Database Connection (Placeholder for Phase 2)
const connectDB = async () => {
  try {
    await mongoose.connect(process.env.MONGO_URI);
    console.log('✅ MongoDB Connected');
  } catch (error) {
    console.error('❌ Database Connection Error:', error);
    process.exit(1);
  }
};
connectDB();

// Health Check Route
app.get('/', (req, res) => {
    res.status(200).json({
        status: 'success',
        message: '🌊 Ganga API is flowing correctly',
        timestamp: new Date().toISOString()
    });
});

// API Routes (Placeholder)
app.use('/api/v1/test', (req, res) => {
    res.json({ message: "Test route working" });
});

// Global Error Handler
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({
        success: false,
        message: 'Internal Server Error',
        error: process.env.NODE_ENV === 'development' ? err.message : undefined
    });
});

// Start Server
app.listen(PORT, () => {
    console.log(`🚀 Server running on http://localhost:${PORT}`);
});