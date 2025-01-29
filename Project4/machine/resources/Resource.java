package machine.resources;

public abstract class Resource {

    private int count;

    protected Resource() {

    }

    protected Resource(int count) {
        if (isSet(count)) {
            this.count = count;
        }
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        if (isSet(count)) {
            this.count = count;
        }
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    protected final boolean isSet(int count) {
        return count >= 0;
    }

    public void add(int count) {
        if (isSet(count)) {
            setCount(count + getCount());
        }
    }

    public void reduce(int count) {
        if (isSet(count)) {
            setCount(getCount() - count);
        }
    }
}
