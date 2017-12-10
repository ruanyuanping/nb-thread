package com.namibank.Thread_001;

public class MyThread  extends Thread{

	private int count = 5;
	
	@Override
	public synchronized void run() {
		count--;
		System.out.println(this.currentThread().getName()+"-"+count);
	}

	public static void main(String[] args) {
		MyThread myThread = new MyThread();
		
		Thread thread1 = new Thread(myThread,"t1"); 
		Thread thread2 = new Thread(myThread,"t2");
		Thread thread3 = new Thread(myThread,"t3");
		Thread thread4 = new Thread(myThread,"t4");
		Thread thread5 = new Thread(myThread,"t5");
		
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		thread5.start();
	}

}
