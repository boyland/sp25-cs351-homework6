import java.util.Random;
import java.awt.Point;

import edu.uwm.cs.junit.EfficiencyTestCase;
import edu.uwm.cs351.LinkedSequence;

public class TestEfficiency extends EfficiencyTestCase {

	Point t1 = new Point(1,2);
	Point t2 = new Point(2,4);
	Point t3 = new Point(3,6);
	Point t4 = new Point(4,8);
	Point t5 = new Point(5,10);
	Point t6 = new Point(6,12);
	Point t7 = new Point(7,14);
	Point t8 = new Point(8,15);

	Point t[] = {null, t1, t2, t3, t4, t5, t6, t7, t8};
	
	LinkedSequence<Point> s;
	Random r;
	
	@Override
	public void setUp() {
		s = new LinkedSequence<Point>();
		r = new Random();
		try {
			assert 1/(int)(s.size()) == 42 : "OK";
			assertTrue(true);
		} catch (ArithmeticException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
		super.setUp();
	}

	private static final int POWER = 20;
	private static final int MAX_LENGTH = 1 << POWER; // > 1 million 
	
	
	public void test0() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(i, s.size());
			s.addBefore(t[i%t.length]);
		}
	}
	
	public void test1() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addAfter(t[i%t.length]);
			s.advance(); 
			assertFalse(s.isCurrent());
		}
		assertEquals(MAX_LENGTH, s.size());
	}
	
	public void test2() {
		for (int i=0; i < MAX_LENGTH; i += 2) {
			s.addBefore(t[i%t.length]);
			s.addBefore(t[i%t.length]);
			s.advance();
		}
		assertEquals(MAX_LENGTH, s.size());
	}
	
	public void test3() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addAfter(t[i%t.length]);
		}
		int j = 0;
		s.start();
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertTrue(s.isCurrent());
			assertEquals(t[j], s.getCurrent());
			s.advance();
			if (++j == t.length) j = 0;
		}
		assertFalse(s.isCurrent());
	}
	
	public void test4() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(i, s.size());
			s.addBefore(t[i%t.length]);
		}
		for (int i=0; i < MAX_LENGTH; i += 2) {
			s.removeCurrent();
		}
		assertEquals(MAX_LENGTH/2, s.size());
	}
	
	public void test5() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(i, s.size());
			s.addBefore(t[i%t.length]);
		}
		for (int i=0; i < MAX_LENGTH; i += 2) {
			s.removeCurrent();
			s.advance();
		}
		assertEquals(MAX_LENGTH/2, s.size());
	}
	
	public void test6() {
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			s.addAfter(t[i%t.length]);
			s.advance(); 
			assertFalse(s.isCurrent());
		}
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			s.addBefore(t[i%t.length]);
			s.removeCurrent();
		}
		assertEquals(MAX_LENGTH/2, s.size());
	}
	
	public void test7() {
		LinkedSequence<Point> other = new LinkedSequence<Point>();
		for (int i=0; i < t.length; ++i) {
			other.addBefore(t[i]);
		}
		for (int i=0; i < MAX_LENGTH; i += t.length) {
			s.addAll(other);
			if (s.isCurrent()) s.advance();
			else s.start();
		}
		int n = MAX_LENGTH + (t.length-MAX_LENGTH%t.length)%t.length;
		assertEquals(n, s.size());
	}
		
	public void test8() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addAfter(t[i%t.length]);
			s.advance();
		}
		LinkedSequence<Point> other = s.clone();
		other.start();
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			assertSame(t[i % t.length], other.getCurrent());
			other.advance();
		}
		other.removeCurrent();
		assertEquals(MAX_LENGTH-1, other.size());
		assertEquals(MAX_LENGTH, s.size());
		s.start();
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			s.advance();
		}
		assertSame(t[(MAX_LENGTH/2)%t.length], s.getCurrent());
	}
	
	public void test9() {
		LinkedSequence<Point> other = new LinkedSequence<Point>();
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			s.addAfter(t[i%t.length]);
		}
		for (int i=MAX_LENGTH/2; i < MAX_LENGTH; ++i) {
			other.addAfter(t[i%t.length]);
		}
		s.addAll(other);
		assertEquals(MAX_LENGTH, s.size());
	}
}
