package ch.zhaw.psit4.martin.api.util;

/**
 * Pair Collection as a helper Class for parameter passing
 * 
 * @version 0.0.1-SNAPSHOT
 */
public class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {

    public final T1 first;
    public final T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public static <T1, T2> Pair<T1, T2> of(T1 first,
            T2 second) {
        return new Pair<>(first, second);
    }

    @Override
    public int compareTo(Pair<T1, T2> o) {
        int cmp = compare(first, o.first);
        return cmp == 0 ? compare(second, o.second) : cmp;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static int compare(Object o1, Object o2) {
        return o1 == null ? o2 == null ? 0 : -1
                : o2 == null ? +1 : ((Comparable) o1).compareTo(o2);
    }

    @Override
    public int hashCode() {
        return 31 * hashCodeHelper(first) + hashCodeHelper(second);
    }

    // TODO: move this to a helper class.
    private static int hashCodeHelper(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    @SuppressWarnings("rawtypes")
	@Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        if (this == obj)
            return true;
        return equalHelper(first, ((Pair) obj).first)
                && equalHelper(second, ((Pair) obj).second);
    }

    private boolean equalHelper(Object o1, Object o2) {
        return o1 == null ? o2 == null : (o1 == o2 || o1.equals(o2));
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }
}
