package stack;

/**
 * @author yangbo
 * @date 2019/9/6
 */
public interface Stack<E> {

    int getSize();

    boolean isEmpty();

    void push(E e);

    E pop();

    E peek();

}
