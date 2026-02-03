import express from 'express';
import { addOrderItems, getMyOrders, cancelOrder } from '../controllers/orderController.js';
import { protect } from '../middleware/authMiddleware.js';

const router = express.Router();

router.post('/', protect, addOrderItems);
router.get('/myorders', protect, getMyOrders);
router.put('/:id/cancel', protect, cancelOrder);

export default router;