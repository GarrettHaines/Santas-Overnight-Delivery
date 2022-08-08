package data;

public class Leaderboard
{
    // Leaderboard class for type User, utilizes max heap structure
    // Variables for board capacity and number of items in the heap
    private int capacity = 20;
    private int items = 0;

    // Array of type User containing leaderboard
    private User[] board = new User[capacity];
    
    // Return amount of items in board
    // O(1)
    public int getItems ( )
    {
        return items;
    }
    

    // Variables to obtain indeces of children and parents
    // O(1)

    private int getLeftChildIndex (int parentIndex)
    {
        return 2 * parentIndex + 1;
    }

    private int getRightChildIndex (int parentIndex)
    {
        return 2 * parentIndex + 2;
    }

    private int getParentIndex (int childIndex)
    {
        return (childIndex - 1) / 2;
    }

    // Variables to determine presence of children or parent
    // O(1)

    private boolean hasLeftChild (int index)
    {
        return getLeftChildIndex(index) < items;
    }

    private boolean hasRightChild (int index)
    {
        return getRightChildIndex(index) < items;
    }

    private boolean hasParent (int index)
    {
        return getParentIndex(index) >= 0;
    }

    // Variables to obtain children and parent users
    // O(1)

    private User leftChild (int index)
    {
        return board[getLeftChildIndex(index)];
    }

    private User rightChild (int index)
    {
        return board[getRightChildIndex(index)];
    }

    private User parent (int index)
    {
        return board[getParentIndex(index)];
    }

    
    // Heap methods

    // Add user to leaderboard if qualifying, calls heapUp( )
    // O(log(n))
    public void add (User newUser)
    {
        User tail = getTail( );

        // If the leaderboard is not full, add user to the board
        if (capacity > items)
        {
            board[items] = newUser;
            items++;
            heapUp( );
        }
        // Else, check to see if user qualifies to enter leaderboard
        else if (tail.highScore < newUser.highScore)
        {
            board[items-1] = newUser;
            heapUp( );
        }
    }
    
    // Delete user from leaderboard, calls heapDown( )
    // O(log(n))
    public void delete (String name)
    {
        for (int i = 0; i < 15 && i < items; i++)
        {
            if (board[i].username.equals(name))
            {
                swap(i, items - 1);
                items--;
                heapDown( );
                break;
            }
        }
    }
    
    // Swap two given users
    // O(1)
    private void swap (int indexOne, int indexTwo)
    {
        User temp = board[indexOne];
        board[indexOne] = board[indexTwo];
        board[indexTwo] = temp;
    }

    // Retrieve top user in leaderboard, does not remove user from array
    // O(1)
    public User getHead ( )
    {
        if (items == 0)
            return null;

        User head = board[0];
        return head;
    }

    // Retrieve last user in leaderboard, does not remove user from array
    // O(1)
    public User getTail ( )
    {
        if (items == 0)
            return null;

        User tail = board[items - 1];
        return tail;
    }

    // Retrieve first user in leaderboard, removes user from leaderboard and calls heapDown
    // O(1)
    public User pull ( )
    {
        if (items == 0)
            return null;
        User head = board[0];
        board[0] = board[items - 1];
        items--;
        heapDown( );
        return head;
    }

    // Fix heap from bottom up
    // O(log(n))
    public void heapUp ( )
    {
        if (items != 0 && items != 1)
        {
            int index = items - 1;

            // While given user's score is greater than parent's, heap up
            while (hasParent(index) && parent(index).highScore < board[index].highScore)
            {
                swap(getParentIndex(index), index);
                index = getParentIndex(index);
            }
        }
    }

    // Fix heap from top to bottom
    // O(log(n))
    public void heapDown ( )
    {
        int index = 0;

        while (hasLeftChild(index))
        {
            int biggerChildIndex = getLeftChildIndex(index);

            // Identify child with greater score
            if (hasRightChild(index) && rightChild(index).highScore > leftChild(index).highScore)
            {
                biggerChildIndex = getRightChildIndex(index);
            }

            // If given user's score is greater than the bigger child's, break loop
            if (board[index].highScore > board[biggerChildIndex].highScore)
            {
                break;
            }
            // Else, swap users
            else
            {
                swap(index, biggerChildIndex);
            }

            // Update index
            index = biggerChildIndex;
        }
    }
}