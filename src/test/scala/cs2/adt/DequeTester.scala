package cs2.adt

import org.junit._
import org.junit.Assert._

class DequeTester {

    var d:Deque[Int] = null
    
    @Before def init():Unit = {
        d = Deque[Int]()
    }

    @Test def testPrepend():Unit = {
        for(x <- 1 to 100) {
            d.prepend(x)
            assertTrue(d.peekFront() == x)
        }
        assertFalse(d.isEmpty())
    }

    @Test def testAppend():Unit = {
        for(x <- 1 to 100) {
            d.append(x)
            assertTrue(d.peekBack() == x)
        }
        assertFalse(d.isEmpty())
    }

    @Test def testFront():Unit = {
        for(x <- 1 to 100) {
            d.prepend(x)
            assertTrue(d.front() == x)
        }
        assertTrue(d.isEmpty())
    }

    @Test def testBack():Unit = {
        for(x <- 1 to 100) {
            d.append(x)
            assertTrue(d.back() == x)
        }
        assertTrue(d.isEmpty())
    }

    //the following might be redundant?

    @Test def testPeekFront():Unit = {
        for(x <- 50 to 1759) {
            d.prepend(x)
            assertTrue(d.peekFront() == x)
        }
    }

    @Test def testPeekBack():Unit = {
        for(x <- 50 to 1759) {
            d.append(x)
            assertTrue(d.peekBack() == x)
        }
    }

    @Test def testIsEmpty():Unit = {
        assertTrue(d.isEmpty())
        for(x <- 10 to 175) {
            d.append(x)
            assertFalse(d.isEmpty())
        }
        assertFalse(d.isEmpty())
    }

    @Test def testPeekFrontPeekBackEqual():Unit = {
        d.append(69)
        assertTrue(d.peekBack() == d.peekFront())
    }

}