import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs351.LinkedSequence;
import junit.framework.TestCase;

@SuppressWarnings("unchecked")
public class TestInvariant extends TestCase {
	protected LinkedSequence.Spy spy = new LinkedSequence.Spy();
    protected int reports;
    
    protected void assertReporting(boolean expected, Supplier<Boolean> test) {
            reports = 0;
            Consumer<String> savedReporter = spy.getReporter();
            try {
                    spy.setReporter((String message) -> {
                            ++reports;
                            if (message == null || message.trim().isEmpty()) {
                                    assertFalse("Uninformative report is not acceptable", true);
                            }
                            if (expected) {
                                    assertFalse("Reported error incorrectly: " + message, true);
                            }
                    });
                    assertEquals(expected, test.get().booleanValue());
                    if (!expected) {
                            assertEquals("Expected exactly one invariant error to be reported", 1, reports);
                    }
                    spy.setReporter(null);
            } finally {
                    spy.setReporter(savedReporter);
            }
    }
    
   	private static int unique = 0;
   	/**
   	 * A class used for elements of the linked collection.
   	 * This class is designed to make its methods useless.
   	 */
    private class X {
    	public int value;
    	X(int v) { value = v; }
    	@Override // implementation
    	public String toString() {
    		return "X(" + value + ")#" + (++unique);
    	}
    	@Override // implementation
    	public int hashCode() {
    		return 0;
    	}
    	@Override // implementation
    	public boolean equals(Object x) {
    		assertFalse("Should not have called equals on elements", true);
    		return false;
    	}
    }
    
    X[] e = new X[] {null,     new X(1), new X(2), new X(3), new X(4), 
    				 new X(5), new X(6), new X(7), new X(8), new X(9) };
    LinkedSequence.Spy.Node<X> n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;
    LinkedSequence.Spy.Node<X> d0, d1, d2;

    @Override
    protected void setUp() {
    	n0 = spy.makeNode(null);
    	n1 = spy.makeNode(e[1]);
    	n2 = spy.makeNode(e[2]);
    	n3 = spy.makeNode(e[3]);
    	n4 = spy.makeNode(e[4]);
    	n5 = spy.makeNode(e[5]);
    	n6 = spy.makeNode(e[6]);
    	n7 = spy.makeNode(e[7]);
    	n8 = spy.makeNode(e[8]);
    	n9 = spy.makeNode(e[9]);
    	d0 = spy.makeSelfRef();
    	d1 = spy.makeSelfRef();
    	d2 = spy.makeSelfRef();
    }
    
    LinkedSequence<X> lc;
    
    void assertWellFormed(boolean b, LinkedSequence<?> lc) {
    	assertReporting(b, () -> spy.wellFormed(lc));
    }
    
    
    public void testA0() {
    	spy.linkForward(d0, d0);
    	spy.linkBackward(d0, d0);
    	lc = spy.newInstance(d0, 0, null);
    	assertWellFormed(true, lc);
    }
    
    public void testA1() {
    	lc = spy.newInstance(null, 0, null);
    	assertWellFormed(false, lc);
    }
    
    public void testA2() {
    	lc = spy.newInstance(d1, 0, null);
    	assertWellFormed(false, lc);
    }
    
    public void testA3() {
    	spy.linkForward(d2, d2);
    	lc = spy.newInstance(d2, 0, null);
    	assertWellFormed(false, lc);
    }
    
    public void testA4() {
    	spy.linkBackward(d2, d2);
    	lc = spy.newInstance(d2, 0, null);
    	assertWellFormed(false, lc);
    }
    
    public void testA5() {
    	spy.linkForward(n0, n0);
    	spy.linkBackward(n0, n0);
    	lc = spy.newInstance(n0, 0, null);
    	assertWellFormed(false, lc);
    }
    
    public void testA6() {
    	spy.linkForward(n1, n1);
    	spy.linkBackward(n1, n1);
    	lc = spy.newInstance(n1, 0, null);
    	assertWellFormed(false, lc);
    }
    
    public void testA7() {
    	spy.linkForward(d0, d1, d0);
    	spy.linkBackward(d0, d1, d0);
    	lc = spy.newInstance(d0, 0, null);
    	assertWellFormed(false, lc);
    }
    
    public void testA8() {
    	spy.linkForward(d0, d0);
    	spy.linkBackward(d0, d1, d0);
    	lc = spy.newInstance(d0, 0, null);
    	assertWellFormed(false, lc);    	
    }
    
    public void testA9() {
    	spy.linkForward(d0, d0);
    	spy.linkBackward(d0, d0);
    	spy.assignData(d0, d1);
       	spy.linkForward(d1, d1);
    	spy.linkBackward(d1, d1);
    	lc = spy.newInstance(d0, 0, null);
    	assertWellFormed(false, lc);
    }
    

    public void testB0() {
    	spy.linkForward(d0, n0, d0);
    	spy.linkBackward(d0, n0, d0);
    	lc = spy.newInstance(d0, 1, null);
    	assertWellFormed(true, lc);
    }
    
    public void testB1() {
    	spy.linkForward(d1, n1, d1);
    	spy.linkBackward(d1, n1, d1);
    	lc = spy.newInstance(d1, 1, null);
    	assertWellFormed(true, lc);
    }
    
    public void testB2() {
    	spy.linkForward(d1, n2, d1);
    	spy.linkBackward(d1, n2, d1);
    	lc = spy.newInstance(d1, 0, null);
    	assertWellFormed(false, lc);
    }
    
    public void testB3() {
    	spy.linkForward(d1, n3, d1);
    	spy.linkBackward(d1, n3, d1);
    	lc = spy.newInstance(d1, 2, null);
    	assertWellFormed(false, lc);
    }
    
    public void testB4() {
    	spy.linkForward(d0, d0);
    	spy.linkBackward(d0, d0);
    	lc = spy.newInstance(d0, 1, null);
    	assertWellFormed(false, lc);
    }

    public void testB5() {
    	spy.linkForward(n0, n5, n0);
    	spy.linkBackward(n0, n5, n0);
    	lc = spy.newInstance(n0, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testB6() {
    	spy.linkForward(d0, n6, d0);
    	spy.linkBackward(d0, n6, d0);
    	spy.linkForward(d1, n6, d1);
    	spy.linkBackward(d1, n6, d1);
    	spy.assignData(d1, d0);
    	lc = spy.newInstance(d1, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testB7() {
    	spy.linkForward(d1, n7, n7);
    	spy.linkBackward(d1, n7, d1);
    	lc = spy.newInstance(d1, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testB8() {
    	spy.linkForward(n8, d1);
    	spy.linkBackward(d1, n8, d1);
    	lc = spy.newInstance(d1, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testB9() {
    	spy.linkForward(d1, n9, d1);
    	spy.linkBackward(d1, n8, d1);
    	lc = spy.newInstance(d1, 1, null);
    	assertWellFormed(false, lc);
    }

    
    public void testC0() {
    	spy.linkForward(d0, n0);
    	spy.linkBackward(d0, n0, d0);
    	lc = spy.newInstance(d0, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testC1() {
    	spy.linkForward(d0, n1, d0);
    	spy.linkBackward(d0, n1);
    	lc = spy.newInstance(d0, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testC2() {
    	spy.linkForward(d0, n2, d0);
    	spy.linkBackward(n2, d0);
    	lc = spy.newInstance(d0, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testC3() {
    	spy.linkForward(d0, n3, d0);
    	spy.linkBackward(n3, d0);
    	spy.linkForward(d1, n3);
    	spy.linkBackward(d1, n3, d1);
    	lc = spy.newInstance(d0, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testC4() {
    	spy.linkForward(d1, n4, d1);
    	spy.linkBackward(d1, d1, n4);
    	lc = spy.newInstance(d1, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testC5() {
    	spy.linkForward(d1, n5, d1);
    	spy.linkBackward(n5, n5, d1);
    	lc = spy.newInstance(d1, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testC6() {
    	spy.linkForward(d1, d2, d1);
    	spy.linkBackward(d1, d2, d1);
    	lc = spy.newInstance(d1, 1, null);
    	assertWellFormed(false, lc);
    }
    
   
    public void testD0() {
    	spy.linkForward(d0, n4, n0, d0);
    	spy.linkBackward(d0, n4, n0, d0);
    	lc = spy.newInstance(d0, 2, null);
    	assertWellFormed(true, lc);
    }
    
    public void testD1() {
    	spy.linkForward(d0, n0, n1, d0);
    	spy.linkBackward(d0, n0, n1, d0);
    	lc = spy.newInstance(d0, 2, null);
    	assertWellFormed(true, lc);
    }
    
    public void testD2() {
    	spy.linkForward(d1, n4, n2, d1);
    	spy.linkBackward(d1, n4, n2, d1);
    	lc = spy.newInstance(d1, 1, null);
    	assertWellFormed(false, lc);
    }
    
    public void testD3() {
    	spy.linkForward(d1, n4, n3, d1);
    	spy.linkBackward(d1, n4, n3, d1);
    	lc = spy.newInstance(d1, 3, null);
    	assertWellFormed(false, lc);
    }
    
    public void testD4() {
    	spy.linkForward(d1, n4, n3, d0);
    	spy.linkBackward(d1, n4, n3, d0);
    	lc = spy.newInstance(d1, 2, null);
    	assertWellFormed(false, lc);
    }
    
    public void testD5() {
    	spy.linkForward(d1, n4, n5, d1);
    	spy.linkBackward(d1, n5, n4, d1);
    	lc = spy.newInstance(d1, 2, null);
    	assertWellFormed(false, lc);
    }
    
    public void testD6() {
    	spy.linkForward(d1, n4, n6, d1);
    	spy.linkBackward(d1, n4);
    	spy.linkBackward(n6, d1);
    	lc = spy.newInstance(d1, 2, null);
    	assertWellFormed(false, lc);
    }
    
    public void testD7() {
    	spy.linkForward(d1, n4, n7, d1);
    	spy.linkBackward(d1, n4);
    	spy.linkBackward(d1, n7, d1);
    	lc = spy.newInstance(d1, 2, null);
    	assertWellFormed(false, lc);
    }
    
    public void testD8() {
    	spy.linkForward(d1, n4, n8, n4);
    	spy.linkBackward(d1, n4, n8, d1);
    	lc = spy.newInstance(d1, 2, null);
    	assertWellFormed(false, lc);
    }
    
    public void testD9() {
    	spy.linkForward(d1, n4, n9, n4);
    	spy.linkBackward(n9, n4, n9, d1);
    	lc = spy.newInstance(d1, 2, null);
    	assertWellFormed(false, lc);
    }
    
    
    public void testE0() {
    	spy.linkForward( d2, n5, n0, n9, d2);
    	spy.linkBackward(d2, n5, n0, n9, d2);
    	lc = spy.newInstance(d2, 3, null);
    	assertWellFormed(true, lc);
    }
    
    public void testE1() {
    	spy.linkForward( n2, n5, n1, n9, n2);
    	spy.linkBackward(n2, n5, n1, n9, n2);
    	lc = spy.newInstance(n2, 3, null);
    	assertWellFormed(false, lc);
    }
    
    public void testE2() {
    	spy.linkForward( d2, n5, n2, n0, n2);
    	spy.linkBackward(d2, n5, n2, n0);
    	lc = spy.newInstance(d2, 3, null);
    	assertWellFormed(false, lc);
    }
    
    public void testE3() {
    	spy.linkForward( d2, n5, n3, n9, n5);
    	spy.linkBackward(d2, n5, n3, n9, n5);
    	lc = spy.newInstance(d2, 3, null);
    	assertWellFormed(false, lc);
    }
    
    public void testE4() {
    	spy.linkForward( d2, n5, n4, n9, d2);
    	spy.linkBackward(d2, n5, n4, n9, d2);
    	lc = spy.newInstance(d2, 4, null);
    	assertWellFormed(false, lc);
    }
    
    public void testE5() {
    	spy.linkForward( d2, n5, n0, n9, d2);
    	spy.linkBackward(d2, n5, n0);
    	spy.linkBackward(n9, d2);
    	lc = spy.newInstance(d2, 3, null);
    	assertWellFormed(false, lc);
    }
    
    public void testE6() {
    	spy.linkForward( d2, n5, n6, d1, d2);
    	spy.linkBackward(d2, n5, n6, d1, d2);
    	lc = spy.newInstance(d2, 3, null);
    	assertWellFormed(false, lc);
    }

    public void testE7() {
    	spy.linkForward( d2, n5, n7, n9, d2);
    	spy.linkBackward(d2, n5, n7);
    	spy.linkBackward(n5, n9, d2);
    	lc = spy.newInstance(d2, 3, null);
    	assertWellFormed(false, lc);
    }

    public void testE8() {
    	spy.linkForward( d2, n5, n8, n6, n9, d2);
    	spy.linkBackward(d2, n5, n8, n6, n9, d2);
    	lc = spy.newInstance(d2, 3, null);
    	assertWellFormed(false, lc);
    }
    
    public void testE9() {
    	spy.linkForward( d2, n5, n9, n6, n0, d2);
    	spy.linkBackward(d2, n5, n9, n6, n0, d2);
    	lc = spy.newInstance(d2, 4, null);
    	assertWellFormed(true, lc);
    }
    
    


}
