package cs2.adt

class TheDeque[A : Manifest] extends Deque[A] {
    private class Node(var data:A, var prev:Node, var next:Node)
    private var sentinel:Node = new Node (Array.ofDim[A](1).apply(0), null, null)
    sentinel.prev = sentinel
    sentinel.next = sentinel

    //should add a single element to the logical "beginning" of the Deque
    def prepend(elem:A) = {
        val temp = sentinel.next
        sentinel.next = new Node(elem, sentinel, sentinel.next)
        temp.prev = sentinel.next
    }

    //should add a single element to the logical "end" of the Deque
    def append(elem:A) = {
        val temp = sentinel.prev
        sentinel.prev = new Node(elem, sentinel.prev, sentinel)
        temp.next = sentinel.prev
    } 

    //should return AND remove a single element from the logical "beginning" of the Deque
    def front():A = {
        val ret = sentinel.next.data
        sentinel.next.next.prev = sentinel
        sentinel.next = sentinel.next.next
        ret
    } 

    //should return AND remove a single element from the logical "end" of the Deque
    def back():A = {
        val ret = sentinel.prev.data
        sentinel.prev.prev.next = sentinel
        sentinel.prev = sentinel.prev.prev
        ret
    }

    //should return BUT NOT remove a single element from the logical "beginning"
    def peekFront():A = { sentinel.next.data }

    //should return BUT NOT remove a single element from the logical "end"
    def peekBack():A = { sentinel.prev.data }

    //should return true if the Deque contains no elements, and false otherwise
    def isEmpty():Boolean = { sentinel.prev == sentinel.next && sentinel.data == sentinel.next.data && sentinel.data == sentinel.prev.data }
}