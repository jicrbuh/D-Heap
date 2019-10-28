
/**
 * D-Heap
 * Submitted by:
 * Oren Gal – 302378633 – orengal1
 * Chen Barnoy – 308547108 - chenbarnoy
 */

public class DHeap {

    private int size, max_size, d;
    private DHeap_Item[] array;

    // Constructor of an empty heap
    // m_d >= 2, m_size > 0
    DHeap(int m_d, int m_size) {
        max_size = m_size;
        d = m_d;
        array = new DHeap_Item[max_size];
        size = 0;
    }

    /**
     * public int getSize()
     *
     * Returns the number of elements in the heap.
     *
     * Complexity: O(1)
     */
    public int getSize() {
        return this.size;
    }

    /**
     * public int arrayToHeap()
     * <p>
     * The function builds a new heap from the given array.
     * Previous data of the heap should be erased.
     * pre-conidtion: array1.length() <= max_size
     * postcondition: isHeap()
     * size = array.length()
     * Returns number of comparisons along the function run.
     *
     * Complexity: O(n) (using heapify-down and not insert)
     */
    public int arrayToHeap(DHeap_Item[] array1) {
        size = array1.length;
        //copies from array1 to the heap and updates DHeap_Item position
        for (int i=0;i<size; i++) {
        	array[i] = array1[i];
        	array[i].setPos(i);
        }
     // if array1.length == 0/1 it is a heap already
        if (size == 0 || size == 1) {
            return 0;
        }
        int comparisons = 0;
        // heapifyDown for all the inner vertices from the last to first
        // while keeping track of the number of comparisons made in the process
        for (int i = getLeavesStartIndex() - 1; i > -1; i--) {
            comparisons += this.heapifyDown(array1[i]);
        }
        return comparisons;
    }


    /**
     * public boolean isHeap()
     * <p>
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property or has size == 0.
     *
     * Complexity: O(n*d)
     */
    public boolean isHeap() {
        //empty heap is a legal heap and heap of size 1 is ok too
        if (size == 0 || size == 1) {
            return true;
        }
        //check if all inner vertices follow the "heap rule"
        for (int i = 0; i < getLeavesStartIndex(); i++) {
            if (hasLegalChildren(array[i]) != -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     * <p>
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     * <p>
     * The methods compute the index of the parent and the k-th child of
     * vertex i in a complete D-ary tree stored in an array.
     * Note that indices of arrays in Java start from 0.
     *
     * Complexity: O(1)
     */
    public static int parent(int i, int d) {
        return (i - 1) / d;
    }

    public static int child(int i, int k, int d) {
        return d * i + k;
    }

    /**
     * public int Insert(DHeap_Item item)
     * <p>
     * Inserts the given item to the heap.
     * Returns number of comparisons during the insertion.
     * <p>
     * precondition: item != null
     * isHeap()
     * size < max_size
     * <p>
     * postcondition: isHeap()
     *
     * Complexity: O(logn)
     */
    public int Insert(DHeap_Item item) {
        // insert item at the last position and then heapify-up the item
        this.array[size] = item;
        item.setPos(size);
        this.size++;
        return heapifyUp(item);
    }

    /**
     * public int Delete_Min()
     * <p>
     * Deletes the minimum item in the heap.
     * Returns the number of comparisons made during the deletion.
     * <p>
     * precondition: size > 0
     * isHeap()
     * <p>
     * postcondition: isHeap()
     *
     * Complexity: O(dlogn)
     */
    public int Delete_Min() {
        // change pointer of the minimum item (which means the item at position 0)
        // to point to the last item, then heapify down for the item and decrease
        // the size field
    	//
        array[0] = array[size - 1];
        array[0].setPos(0);
        size--;
        return heapifyDown(array[0]);
    }


    /**
     * public DHeap_Item Get_Min()
     * <p>
     * Returns the minimum item in the heap.
     * <p>
     * precondition: heapsize > 0
     * isHeap()
     * size > 0
     * <p>
     * postcondition: isHeap()
     *
     * Complexity: O(1)
     */
    public DHeap_Item Get_Min() {
        return this.array[0];
    }

    /**
     * public int Decrease_Key(DHeap_Item item, int delta)
     * <p>
     * Decerases the key of the given item by delta.
     * Returns number of comparisons made as a result of the decrease.
     * <p>
     * precondition: item.pos < size;
     * item != null
     * isHeap()
     * <p>
     * postcondition: isHeap()
     *
     * Complexity: O(logn)
     */
    public int Decrease_Key(DHeap_Item item, int delta) {
        // decrease the key and then heapify up
        item.setKey(item.getKey() - delta);
        return heapifyUp(item);
    }

    /**
     * public int Delete(DHeap_Item item)
     * <p>
     * Deletes the given item from the heap.
     * Returns number of comparisons during the deletion.
     * <p>
     * precondition: item.pos < size;
     * item != null
     * isHeap()
     * <p>
     * postcondition: isHeap()
     *
     * Complexity: O(dlogn)
     */
    public int Delete(DHeap_Item item) {
        // keep comparisons counter which will be updated through the process of the method
        int comparisons = 0;
        this.size--;
        // set key to a special value which is smaller than any integer
        item.setKey(Integer.MIN_VALUE);
        // heapify up the item and it will surely become the minimum
        comparisons += heapifyUp(item);
        // delete the minimum which is the item
        comparisons += Delete_Min();
        return comparisons;
    }

    /**
     * public static int DHeapSort(int[] array1, int d)
     * <p>
     * Sort the input array using heap-sort (build a heap, and
     * perform n times: get-min, del-min).
     * Sorting should be done using the DHeap, name of the items is irrelevant.
     * <p>
     * Returns the number of comparisons performed.
     * <p>
     * postcondition: array1 is sorted
     *
     * Complexity: O(d*nlogn) (where n is array1.length)
     */
    public static int DHeapSort(int[] array1, int d) {
        // create a new heap with max length needed (which is array1.length)
        DHeap myheap = new DHeap(d, array1.length);
        //heap.array = new DHeap_Item[array1.length]; //maybe unnecessary 
        
        // iterate over array1's elements and add them to heap.array
        // as keys of items (with irrelevant info of "") while keeping their order
        for (int i = 0; i < array1.length; i++) {
            myheap.array[i] = new DHeap_Item("", array1[i]);
            myheap.array[i].setPos(i);
        }
        // turn heap.array to a legal heap using the method arrayToHeap
        // and start counting the number of comparisons
        int comparisons = myheap.arrayToHeap(myheap.array);
        
        // iterate over heap.array, in the i'th iteration do:
        // add min's key to array1 at the i'th position and then delete it from heap.array
        // while keeping track of the number of comparisons
        for (int i = 0; i < array1.length; i++) {
            array1[i] = myheap.Get_Min().getKey();
            comparisons += myheap.Delete_Min();
        }
        return comparisons;
    }

    /**
     * public int heapifyDown(DHeap_Item item)
     * <p>
     * compares recursively between the item and its children, and
     * switches their position if they break the heap rule
     * <p>
     * Returns the number of comparisons performed.
     * <p>
     * postcondition: isHeap()
     *
     * Complexity: O(dlogn)
     */
    public int heapifyDown(DHeap_Item item) {
    	int comparison =0;
    	int leavesIdx = getLeavesStartIndex();
    	//comparison = comparison + this.numOfChildren(item);
    	while (item.getPos() < leavesIdx && this.hasLegalChildren(item) != -1){
    		DHeap_Item minimalChild = minChild(item);	
    		comparison = comparison + this.numOfChildren(item);
    		switchItems( minimalChild, item);
    		
    	}
        return comparison;
    } 
    /**
     * public int minChild(DHeap_Item item)
     * <p>
     * returns item's child with the minimal key
     * <p>
     * Complexity: O(d)
     */
    public DHeap_Item minChild(DHeap_Item item){
    	int pos = item.getPos();
    	int childrenNum = numOfChildren(item);
    	int itemkey = item.getKey();
    	int posOfMin=-1;
    	for (int i=0; i<childrenNum;i++){
    		DHeap_Item currItem = array[child(pos,i+1,this.d)];
    		if(itemkey >= currItem.getKey()){
    			posOfMin = child(pos,i+1,this.d);
    			itemkey = currItem.getKey();
    		}
    	}
    	return array[posOfMin];
    }
    
    /**
     * public int numOfChildren(DHeap_Item item)
     * <p>
     * returns the number of item's children
     * <p>
     * Complexity: O(1)
     */
    public int numOfChildren(DHeap_Item item){
    	int pos = item.getPos();
    	int maxChildIdx = child(pos, d, this.d);
    	//if the heap size is larger than the maxChildIIdx than item has d children
    	if (maxChildIdx < this.size - 1) {
    		return this.d;
    	}
    	//else - has less than d children
    	else {
    		return this.d - (maxChildIdx - this.size +1);
    	}
    }
    

    /**
     * public int heapifyUp(DHeap_Item item)
     * <p>
     * compares recursively between the item and its parent, and
     * switches their position if they break the "heap rule"
     * <p>
     * Returns the number of comparisons performed.
     * <p>
     * postcondition: item is following the "heap rule"
     *
     * Complexity: O(logn)
     */
    public int heapifyUp(DHeap_Item item) {
    	int comparison =1;
    	int pos = item.getPos();
    	//if item is root, returns 0
    	if (pos==0) {
    		return 0;
    	}
    	DHeap_Item parent = array[parent(pos, this.d)];
    	while (item.getPos()!= 0 && !this.parentIsLegal(item)) {
    		comparison++;
    		//switching the item with its parent
    		switchItems( item, parent);
    		//going up one level
    		//item = parent;
    		pos = item.getPos();
    		if (pos != 0) {
    			parent = array[parent(pos, this.d)];	
    		}
    	}
        return comparison;
    } 
    /**
     * public void switchItems(DHeap_Item item1,DHeap_Item item2)
     * <p>
     * switches between item and its parent 
     * <p>
     * Complexity: O(1)
     */
    public void switchItems(DHeap_Item item,DHeap_Item parent) {
    	int pos = item.getPos();
    	int parentPos = parent.getPos();
    	//switching the item with its parent
    	array[pos] = parent;
    	parent.setPos(pos);
    	array[parentPos] = item;
    	item.setPos(parentPos);
    }   
    
    /**
     * public boolean parentIsLegal(DHeap_Item item)
     * <p>
     * compares the item to its parent and returns true 
     * iff the heap property is maintained
     * <p>
     * Complexity: O(1)
     */
    public boolean parentIsLegal(DHeap_Item item){
    	int pos = item.getPos();
    	//if item is root, returns true
    	if (pos==0) {
    		return true;
    	}
    	DHeap_Item parent = array[parent(pos, this.d)];
    	if (parent.getKey() > item.getKey()) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    /**
     * public int hasLegalChildren(DHeap_Item item)
     * <p>
     * Returns -1 iff all children of item follow the "heap rule"
     * else, returns the position of the child that breaks the heap rule
     *pre-conditions:
     *isHeap()
     *size>0
     *item.getPos()<size
     *
     * Complexity: O(d)
     */
    public int hasLegalChildren(DHeap_Item item) {  
    	int parentPos = item.getPos();
    	for (int i =0; i<d;i++) {
    		//goes over all possible children (max of d children)
    		int childPosition = child(parentPos,i+1,this.d);
    		if (childPosition < this.size) {
    			if (this.array[childPosition].getKey() < item.getKey()){
    				return this.array[childPosition].getPos();
    			}
    		}
    	}
        return -1;
    }

    /**
     * public int getLeavesStartIndex()
     * <p>
     * calculates the index i for which the indices {i,...,size-1}
     * represent the set of all leaves in the heap
     * <p>
     * returns the index calculated
     *
     * Complexity: O(1)
     */
    public int getLeavesStartIndex() {
        int h = (int) (Math.log(size) / Math.log(d)); //the height of the tree
        int x = (int)(Math.pow(d,h+1)-1)/(d-1); //the number of items in a full heap of height h
        int t = (int) (Math.pow(d,h) - (x-size) + ((x-size)/d)); //number of leaves in the tree
        return size - t;

        //int h = (int)((Math.log(size*(d-1))+1)/Math.log(d));
        //return (int) (Math.pow(d, h) - size) / d + size;
    }

    public String toString() {
        String s = "";
        for (int i=0 ; i<size ; i++) {
            s += array[i];
            if (i%d==0) {
                s += "|";
            }
            else {
                s += ",";
            }
        }
        return s;
    }

    public DHeap_Item[] getArray() {
        return this.array;
    }


}
