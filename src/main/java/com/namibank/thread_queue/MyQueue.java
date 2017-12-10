package com.namibank.thread_queue;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 阻塞队列
 * @author Administrator
 *
 */
public class MyQueue {

	//1： 集合存放数据
	private LinkedList<Object> list = new LinkedList<>();
	
	//最小元素
	private final int minSize = 0;
	
	//最大元素
	private final int maxSize;
	
	//原子性计数器
	private AtomicInteger count = new  AtomicInteger(0);
	
	//构造方法
	private MyQueue (int size){
		this.maxSize = size;
	}
	
	//对象锁
	private final Object lock = new Object();
	
	public void put (Object obj) {
		synchronized (lock){
			if(this.maxSize == count.get()){
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//加入元素
			list.add(obj);
			//计数器递增
			count.incrementAndGet();
			System.out.println("当前放入的值为 ：" + obj);
			//唤醒另外一个线程
			lock.notify();
		}
	}
	
	//take: 取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到BlockingQueue有新的数据被加入.
	public Object take(){
		synchronized (lock){
			if(this.minSize == count.get()){
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//移除第一个值
			Object object  = list.removeFirst();
			System.out.println("当前移除的值为 : " + object);
			//计数器递减
			count.decrementAndGet();
			//唤醒另外一个线程
			lock.notify();
			return object;
		}
	}
	
	public  int getSize (){
		return count.get();
	}
	
	public static void main(String[] args) {
		final MyQueue queue = new MyQueue(5);
		queue.put("a");
		queue.put("b");
		queue.put("c");
		queue.put("d");
		queue.put("e");
		System.out.println("计数器大小为： " + queue.getSize());
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				queue.put("f");
				queue.put("g");
			}
		},"t1");
		
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				queue.take();
				queue.take();
				queue.take();
				queue.take();
			}
		},"t2");
		
		t1.start();
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		t2.start();
	}
}
