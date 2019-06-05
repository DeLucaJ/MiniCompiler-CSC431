package cfg;

public class CFEdge
{
    private CFBlock source;
    private CFBlock sink;

    public CFEdge(CFBlock source, CFBlock sink)
    {
        this.source = source;
        this.sink = sink;
    }

    public CFBlock getSouce()
    {
        return this.source;
    }

    public CFBlock getSink()
    {
        return this.sink;
    }

    public void printEdge()
    {
        System.out.print(source.getLabel() + " -> " + sink.getLabel());
    }
}