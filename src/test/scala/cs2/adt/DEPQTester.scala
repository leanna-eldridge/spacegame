package cs2.adt

import org.junit._
import org.junit.Assert._

class DEPQTester {

    var d:DEPQ[Int] = null

    @Before def init():Unit = {
        d = new DEPQ[Int]()
    }

    @Test def testIsEmpty():Unit = {
        assertTrue(d.isEmpty())
        for(x <- 10 to 175) {
            d.add(x)
            assertFalse(d.isEmpty())
        }
        assertFalse(d.isEmpty())
    }

    @Test def testAdd():Unit = {
        for(x <- 1 to 100) {
            d.add(x)
            assertTrue(d.peekMax() == x)
            assertTrue(d.peekMin() == 1)
        }
        assertFalse(d.isEmpty())
    }

    @Test def testPeekMax():Unit = {
        d.add(3)
        assertTrue(d.peekMax() == 3)
        d.add(10)
        assertTrue(d.peekMax() == 10)
        d.add(7)
        assertTrue(d.peekMax() == 10)
        d.add(1)
        assertTrue(d.peekMax() == 10)
        d.add(11)
        assertTrue(d.peekMax() == 11)
    }

    @Test def testMax():Unit = {
        for(x <- 1 to 100) {
            d.add(x)
            assertTrue(d.max() == x)
        }
        assertTrue(d.isEmpty())
        for(x <- 1 to 100) {
            d.add(x)
        }
        for(x <- 100 to 1 by -1) {
            assertTrue(d.max() == x)
        }
        assertTrue(d.isEmpty())
    }

    @Test def testPeekMin():Unit = {
        d.add(3)
        assertTrue(d.peekMin() == 3)
        d.add(10)
        assertTrue(d.peekMin() == 3)
        d.add(7)
        assertTrue(d.peekMin() == 3)
        d.add(1)
        assertTrue(d.peekMin() == 1)
    }

    @Test def testMin():Unit = {
        for(x <- 1 to 100) {
            d.add(x)
            assertTrue(d.min() == x)
        }
        assertTrue(d.isEmpty())
        for(x <- 1 to 100) {
            d.add(x)
        }
        for(x <- 1 to 100) {
            assertTrue(d.min() == x)
        }
        assertTrue(d.isEmpty())
    }

    @Test def testPeekMinPeekMaxEqual():Unit = {
        d.add(69)
        assertTrue(d.peekMin() == d.peekMax())
    }

}