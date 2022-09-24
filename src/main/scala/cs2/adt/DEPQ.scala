package cs2.adt

class DEPQ[A <% Ordered[A] : Manifest] extends DoubleEndPriorityQueue[A] {
    private class Node(var data:A, var prev:Node, var next:Node)
    private var sentinel:Node = new Node (Array.ofDim[A](1).apply(0), null, null)
    sentinel.prev = sentinel
    sentinel.next = sentinel

    //Return true if there are no elements in the DEPQ
    def isEmpty():Boolean = { sentinel.prev == sentinel.next && sentinel.data == sentinel.next.data && sentinel.data == sentinel.prev.data }
    
    //Add an element to the DEPQ
    def add(elem: A):Unit = { 
        var rover:Node = sentinel
        while(rover.next != sentinel && rover.next.data > elem) {
            rover = rover.next
        }
        rover.next = new Node(elem, rover, rover.next)
        rover.next.next.prev = rover.next
    }

    //Return the largest element in the DEPQ
    def peekMax():A = { sentinel.next.data }

    //Return AND Remove the largest element from the DEPQ
    def max():A = { 
        val ret = sentinel.next.data 
        sentinel.next.next.prev = sentinel
        sentinel.next = sentinel.next.next
        ret
    }

    //Return the smallest element in the DEPQ
    def peekMin():A = { sentinel.prev.data }

    //Return AND Remove the smallest element from the DEPQ
    def min():A = {
        val ret = sentinel.prev.data
        sentinel.prev.prev.next = sentinel
        sentinel.prev = sentinel.prev.prev
        ret
    }

}