import express from 'express';
import { createProduct, getProducts, getProductById } from '../controllers/productController.js';
import { protect, authorize } from '../middleware/authMiddleware.js';

const router = express.Router();

router.get('/', getProducts); // GET /api/v1/products
router.get('/:id', getProductById);
router.post('/', 
    protect,               // 1. Must be logged in
    authorize('vendor', 'admin'),   // 2. Must be a Vendor
    createProduct
); // POST /api/v1/products



export default router;