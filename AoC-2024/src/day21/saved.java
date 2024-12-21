    public static HashSet<String> generatePaths(Board2D board, IntPair from, IntPair to)
    {
        if (board.getCharAtXY(from) == ' ')
        {
            var result = new HashSet<String>();
            return result;
        }
        
        if (from.equals(to))
        {
            var result = new HashSet<String>();
            result.add("");
            return result;
        }

        var d = to.minus(from);

        var result = new HashSet<String>();
        if (d.getX() > 0)
        {
            var paths = generatePaths(board, from.add(IntPair.RIGHT), to);
            for (var p : paths)
            {
                result.add('>'+p);
            }
        }
        if (d.getX() < 0)
        {
            var paths = generatePaths(board, from.add(IntPair.LEFT), to);
            for (var p : paths)
            {
                result.add('<'+p);
            }
        }
        if (d.getY() > 0)
        {
            var paths = generatePaths(board, from.add(IntPair.UP), to);
            for (var p : paths)
            {
                result.add('v'+p);
            }
        }
        if (d.getY() < 0)
        {
            var paths = generatePaths(board, from.add(IntPair.DOWN), to);
            for (var p : paths)
            {
                result.add('^'+p);
            }
        }
        return result;
    }
