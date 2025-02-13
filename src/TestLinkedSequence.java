import edu.uwm.cs351.LinkedSequence;
import junit.framework.TestCase;



public class TestLinkedSequence extends TestCase {
	
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	private LinkedSequence<Integer> s;
	Integer b1 = 1;
	Integer b2 = 2;
	Integer b2a = Integer.valueOf(2);
	Integer b3 = 3;
	Integer b4 = 4;
	Integer b5 = 5;
	
	Integer b[] = { null, b1, b2, b3, b4, b5 };
	
	
	@Override
	public void setUp() {
		s = new LinkedSequence<Integer>();
		try {
			assert 3/s.size() == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (ArithmeticException ex) {
			return;
		}
	}

	
	/// test0x: tests of constructor and empty sequence methods
	
	public void test00() {
		assertEquals(0,s.size());
	}
	
	public void test01() {
		assertFalse(s.isCurrent());
	}
		
	public void test03() {
		assertException(IllegalStateException.class,() -> {s.getCurrent();});		
	}
	
	public void test04() {
		assertException(IllegalStateException.class, () -> {s.advance();});
	}
	
	
	public void test06() {
		s.start();
		assertFalse(s.isCurrent());
	}
	
	
	
	/// test1x: test addBefore
	
	public void test10() {
		s.addBefore(b1);
		assertEquals(1,s.size());
	}
	
	public void test11() {
		s.addBefore(b2);
		assertTrue(s.isCurrent());
	}
	
	public void test12() {
		s.addBefore(b3);
		assertSame(b3, s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test13() {
		s.addBefore(null);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
		assertNull(s.getCurrent());
	}
	
	public void test14() {
		s.addBefore(b4);
		s.addBefore(b1);
		assertEquals(2, s.size());
		assertTrue(s.isCurrent());
		assertSame(b1, s.getCurrent());
	}
	
	public void test15() {
		s.addBefore(b5);
		s.addBefore(b1);
		s.advance();
		assertEquals(2, s.size());
		assertTrue(s.isCurrent());
		assertSame(b5, s.getCurrent());
	}
	
	public void test16() {
		s.addBefore(b1);
		s.advance();
		assertFalse(s.isCurrent()); // reminder!
		s.addBefore(b2); // what does addBefore do if isCurrent is false?
		assertEquals(2, s.size());
		assertTrue(s.isCurrent());
		assertSame(b2, s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b1, s.getCurrent());		
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test17() {
		s.addBefore(b1);
		s.addBefore(b2);
		s.addBefore(b3);
		assertEquals(3, s.size());
		assertTrue(s.isCurrent());
		assertSame(b3, s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b2, s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b1, s.getCurrent());		
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test18() {
		s.addBefore(b1);
		s.addBefore(b2);
		s.advance();
		s.addBefore(b3);
		assertEquals(3, s.size());
		assertTrue(s.isCurrent());
		assertSame(b3, s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b1, s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b2, s.getCurrent());		
	}
	
	public void test19() {
		s.addBefore(b1);
		s.addBefore(b2);
		s.advance();
		s.advance();
		s.addBefore(b3);
		assertEquals(3, s.size());
		assertTrue(s.isCurrent());
		assertSame(b3, s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b2, s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b1, s.getCurrent());		
		s.advance();
		assertFalse(s.isCurrent());
		assertException(IllegalStateException.class, () ->s.advance());
	}

	
	/// test2x: test of addAfter
	
	public void test20() {
		s.addAfter(b1);
		assertEquals(1,s.size());
	}
	
	public void test21() {
		s.addAfter(b1);
		assertTrue(s.isCurrent());
	}
	
	public void test22() {
		s.addAfter(b2);
		assertSame(b2,s.getCurrent());
		s.advance();
		assertException(IllegalStateException.class, () -> s.getCurrent());
	}
	
	public void test23() {
		s.addAfter(null);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
	}
	
	public void test24() {
		s.addAfter(b1);
		s.addAfter(b2);
		assertEquals(2, s.size());
		assertSame(b2,s.getCurrent());
	}
	
	public void test25() {
		s.addAfter(b3);
		s.addAfter(b4);
		s.start();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b4,s.getCurrent());
	}
	
	public void test26() {
		s.addAfter(b5);
		s.advance();
		assertFalse(s.isCurrent());
		s.addAfter(b1); // There is no current, where is it added?
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertException(IllegalStateException.class, () -> s.getCurrent());
	}
	
	public void test27() {
		s.addAfter(b1);
		s.addAfter(null);
		s.addAfter(b3);
		assertEquals(3, s.size());
		s.start();
		assertSame(b1, s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertNull(s.getCurrent());
		s.advance();
		assertSame(b3, s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test28() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.start();
		s.addAfter(b3);
		s.start();
		assertSame(b1, s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b3, s.getCurrent());
		s.advance();
		assertSame(b2, s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test29() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.advance();
		s.addAfter(b3);
		s.start();
		assertSame(b1, s.getCurrent());
		s.advance();
		assertSame(b2, s.getCurrent());
		s.advance();
		assertSame(b3, s.getCurrent());
		s.advance();
		assertException(IllegalStateException.class, () -> s.advance());
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(b1, s.getCurrent());
	}
	
	
	/// test3x: tests of removeCurrent
	
	public void test30() {
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test31() {
		s.addBefore(b3);
		s.start();
		s.removeCurrent();
		assertEquals(0, s.size());
		assertFalse(s.isCurrent());
		assertException(IllegalStateException.class, () -> s.getCurrent());
	}
	
	public void test32() {
		s.addAfter(b2);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test33() {
		s.addBefore(b4);
		s.addBefore(b5);
		s.removeCurrent();
		assertEquals(1, s.size());
		assertSame(b4, s.getCurrent());
		s.advance();
		s.start();
		assertSame(b4,s.getCurrent());
	}
	
	public void test34() {
		s.addBefore(b1);
		s.addBefore(b2);
		s.advance();
		s.removeCurrent();
		assertEquals(1, s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b2, s.getCurrent());
	}
	
	public void test35() {
		s.addBefore(b3);
		s.addBefore(b4);
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test36() {
		s.addBefore(b1);
		s.addBefore(b2);
		s.addBefore(b3);
		s.removeCurrent();
		assertEquals(2, s.size());
		assertSame(b2, s.getCurrent());
		s.advance();
		assertSame(b1, s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test37() {
		s.addBefore(b1);
		s.addBefore(b2);
		s.addBefore(b3);
		s.advance();
		s.removeCurrent();
		assertEquals(2, s.size());
		assertSame(b1, s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b3, s.getCurrent());
	}

	
	public void test38() {
		s.addBefore(b1);
		s.addBefore(b2);
		s.addBefore(b3);
		s.advance();
		s.advance();
		s.removeCurrent();
		assertEquals(2, s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b3, s.getCurrent());
		s.advance();
		assertSame(b2, s.getCurrent());
	}
	
	public void test39() {
		s.addBefore(b1);
		s.addBefore(b2);
		s.addBefore(b3);
		s.advance();
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		assertEquals(3, s.size());
		assertFalse(s.isCurrent());
	}
	

	/// test4x: tests of clone
	
	public void test40() {
		LinkedSequence<Integer> c = s.clone();
		assertFalse(c.isCurrent());
		assertEquals(0, c.size());
	}
	
	public void test41() {
		LinkedSequence<Integer> c = s.clone();
		s.addBefore(b1);
		c.addBefore(b2);
		assertSame(b1, s.getCurrent());
	}
	
	public void test42() {
		s.addBefore(b1);
		LinkedSequence<Integer> c = s.clone();
		
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b1,c.getCurrent()); c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
	}

	public void test43() {
		s.addBefore(b1);
		s.advance();
		LinkedSequence<Integer> c = s.clone();
		
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		s.start();
		c.start();
		assertSame(b1, s.getCurrent());
		assertSame(b1, c.getCurrent());
	}
	
	public void test44() {
		s.addBefore(b1);
		s.advance();
		LinkedSequence<Integer> c = s.clone();
		
		s.addBefore(b2);
		c.start();
		assertSame(b1, c.getCurrent());
		assertSame(b2, s.getCurrent());
		
	}
	
	public void test45() {
		s.addBefore(b2);
		s.addBefore(b1);
		s.advance();
		LinkedSequence<Integer> c = s.clone();
		
		assertSame(b2, s.getCurrent());
		assertSame(b2, c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		s.start();
		c.start();
		assertSame(b1, s.getCurrent());
		assertSame(b1, c.getCurrent());
		
		s.advance();
		c.advance();
		assertSame(b2, c.getCurrent());
		s.addAfter(b3);
		c.advance();
		assertException(IllegalStateException.class, () -> c.getCurrent());
	}
	
	public void test46() {
		s.addBefore(b2);
		s.addBefore(b1);
		s.advance();
		LinkedSequence<Integer> c = s.clone();
		
		assertSame(b2, c.getCurrent());
		s.addAfter(b3);
		c.advance();
		assertException(IllegalStateException.class, () -> c.getCurrent());
		
	}

	public void test47() {
		s.addBefore(b5);
		s.addBefore(b4);
		s.advance();
		s.advance();
		LinkedSequence<Integer> c = s.clone();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		
		c.addBefore(b3);
		s.start();
		assertSame(b3, c.getCurrent());
		assertSame(b4, s.getCurrent());
	}
	
	public void test48() {
		s.addBefore(b1);
		s.addBefore(b1);
		s.addBefore(b2);
		s.addBefore(b3);
		s.addBefore(b5);
		LinkedSequence<Integer> c = s.clone();
		s.start();
		c.start();
		s.removeCurrent();
		assertSame(b5, c.getCurrent()); c.advance();
		assertSame(b3, c.getCurrent()); c.advance();
		assertSame(b2, c.getCurrent()); c.advance();
		assertSame(b1, c.getCurrent()); c.advance();
		assertSame(b1, c.getCurrent()); c.advance();
		assertFalse(c.isCurrent());
		assertSame(b3, s.getCurrent());
	}
	
	public void test49() {
		// If your code fails this test, check that you
		// are following the "clone" idiom in the textbook.
		class MyLinkedSequence extends LinkedSequence<Integer> {
			MyLinkedSequence(boolean ignored) {
				super();
				assert s != null :"Should not use constructor!";
			}
		}
		LinkedSequence<Integer> s2 = new MyLinkedSequence(true);
		s2.addBefore(b3);
		s = null; // make sure that the clone code doesn't deviously call constructor
		LinkedSequence<Integer> c = s2.clone();
		assertTrue("Didn't follow clone pattern!", c instanceof MyLinkedSequence);
		assertSame(b3, c.getCurrent());
	}
	
	
	/// test5n/test6n: tests of (unaliased) addAll
	
	public void test50() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		s.addAll(se);
		assertEquals(0,s.size());
	}

	public void test51() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		s.addBefore(b1);
		s.addAll(se);
		assertEquals(b1,s.getCurrent());
	}

	public void test52() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		s.addAfter(b2);
		s.advance();
		s.addAll(se);
		assertFalse(s.isCurrent());
	}

	public void test53() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		s.addBefore(b3);
		s.addAfter(b4);
		s.addAll(se);
		assertSame(b4,s.getCurrent());
	}

	public void test54() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addBefore(b1);
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertTrue(se.isCurrent());
		assertEquals(1,s.size());
		assertEquals(1,se.size());
		s.start();
		assertSame(b1,s.getCurrent());
		assertSame(b1,se.getCurrent());
	}
	
	public void test55() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addAfter(b1);
		s.addAfter(b2);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test56() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addAfter(b1);
		s.addAfter(b2);
		s.advance();
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertTrue(se.isCurrent());
		assertSame(b1,se.getCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test57() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addAfter(b1);
		se.advance();
		s.addAfter(b3);
		s.addBefore(b2);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertFalse(se.isCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());	
	}
	
	public void test58() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addAfter(b1);
		s.addAfter(b2);
		s.addAfter(b3);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
	}
	
	public void test59() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addAfter(b1);
		s.addAfter(b2);
		s.addAfter(b3);
		s.advance();
		assertFalse(s.isCurrent());
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(b1,se.getCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}

	public void test60() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addAfter(b2);
		se.addBefore(b1);	
		s.addAfter(b4);
		s.addBefore(b3);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
	}

	public void test61() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addAfter(b2);
		se.addBefore(b1);
		se.advance();
		s.addAfter(b3);
		s.addAfter(b4);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(b2,se.getCurrent()); se.advance();
		assertFalse(se.isCurrent());
		// check s
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(b3,s.getCurrent());
	}

	public void test62() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addBefore(b2);
		se.addBefore(b1);
		se.advance();
		se.advance();
		s.addAfter(b3);
		s.addAfter(b4);
		s.advance();
		assertFalse(s.isCurrent());
		assertFalse(se.isCurrent());
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertFalse(se.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		s.start();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
	}

	public void test63() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addAfter(b1);
		se.addAfter(b2);	
		s.addAfter(b3);
		s.addAfter(b4);
		s.addAll(se);
		s.advance();
		s.addAfter(b5);
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertEquals(5,s.size());
		assertEquals(2,se.size());
		assertSame(b2,se.getCurrent());
		se.advance();
		assertFalse(se.isCurrent());
		se.start();
		assertSame(b1,se.getCurrent());
	}
	
	public void test64() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addBefore(null);
		se.addBefore(b4);
		se.addBefore(null);
		s.addBefore(b1);
		s.addAll(se);
		assertEquals(4, s.size());
		assertSame(b1, s.getCurrent()); s.advance();
		assertSame(null, s.getCurrent()); s.advance();
		assertSame(b4, s.getCurrent()); s.advance();
		assertSame(null, s.getCurrent()); s.advance();
		assertException(IllegalStateException.class, () -> s.getCurrent());
	}

	public void test67() {
		s.addBefore(b2);
		s.addBefore(b1);
		s.advance();
		assertException(NullPointerException.class, () -> s.addAll((LinkedSequence<Integer>)null));
		assertSame(b2, s.getCurrent());
	}
	
	public void test69() {
		LinkedSequence<Integer> se = new LinkedSequence<Integer>();
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		// se has 24 elements
		s.addAfter(b1);
		s.addAfter(b2);
		s.addAll(se);
		assertEquals(26,s.size());
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		s.addAll(se);
		assertEquals(50,s.size());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent());
	}
	
	
	/// test7n: tests of self-addAll
	
	public void test70() {
		s.addAll(s);
		assertFalse(s.isCurrent());
		assertEquals(0,s.size());
	}
	
	
	public void test71() {
		s.addAfter(b1);
		s.addAll(s);
		assertEquals(2,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test72() {
		s.addAfter(b1);
		s.advance();
		s.addAll(s);
		assertEquals(2,s.size());
		assertFalse(s.isCurrent());
	}
	
	public void test73() {
		s.addAfter(b1);
		s.removeCurrent();
		s.addAll(s);
		assertEquals(0,s.size());
		assertFalse(s.isCurrent());
	}
	
	public void test74() {
		s.addAfter(b2);
		s.addBefore(b1);
		s.addAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}
	
	public void test75() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.addAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}

	public void test76() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.advance();
		assertFalse(s.isCurrent());
		s.addAll(s);
		assertFalse(s.isCurrent());
		assertEquals(4,s.size());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}

	public void test77() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.addAll(s);
		s.removeCurrent();
		s.addBefore(b3);
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		s.advance();
		s.addAll(s);
		assertEquals(8,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}
	
	public void test79() {
		s.addAfter(b2);
		s.addBefore(b1);
		s.advance();
		s.advance();
		s.addAll(s);
		assertFalse(s.isCurrent());
		s.addAll(s);
		s.addAll(s);
		assertFalse(s.isCurrent());
		assertEquals(16, s.size());
	}
	

}
