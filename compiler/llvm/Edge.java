package llvm;

public class Edge
{
    private Block source;
    private Block sink;

    public Edge(Block source, Block sink)
    {
        this.source = source;
        this.sink = sink;
    }

    public Block getSouce()
    {
        return this.source;
    }

    public Block getSink()
    {
        return this.sink;
    }

    public void printEdge()
    {
        System.out.print(source.getLabel() + " -> " + sink.getLabel());
    }
}