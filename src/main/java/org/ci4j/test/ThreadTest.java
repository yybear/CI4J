package org.ci4j.test;

class Thread1 implements Runnable {
	String s = "world";
	
	@Override
	public void run() {
		while(true) {
		System.out.println(this.hashCode());
		System.out.println("run: " +Thread.currentThread().hashCode());
		System.out.println(Thread.activeCount());
		System.out.println(s);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	public void start() {
		System.out.println("start:" + Thread.currentThread().hashCode());
		
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
		try {
			t.join(9000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s = "hello";
	}

}

public class ThreadTest {
	public static void main(String[] args) {
		System.out.println("main:" + Thread.currentThread().hashCode());
		Thread1 t1 = new Thread1();
		System.out.println(t1.hashCode());
		t1.start();
		
		while(true) {
			
		}
	}  
}