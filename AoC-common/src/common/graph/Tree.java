package common.graph;

public class Tree<TNodeKey, TInfo>
{
    private static class Node<TNodeKey, TInfo>
    {
        private Node<TNodeKey, TInfo> parent;
        private Node<TNodeKey, TInfo> child; 
    }
}
