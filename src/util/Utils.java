package util;

import message.Direction;

public class Utils
{
    public static byte createByte(byte initByte, int pattern, int index)
    {
        pattern = pattern << (index * 2);
        byte patternByte = (byte) (pattern & 0xFF);
        byte helperPattern = 0;
        switch (index)
        {
            case 0:
                helperPattern = (byte) 0xFC;
                break;
            case 1:
                helperPattern = (byte) 0xF3;
                break;
            case 2:
                helperPattern = (byte) 0xCF;
                break;
            case 3:
                helperPattern = (byte) 0x3F;
                break;
        }
        return (byte) ((initByte & helperPattern) | patternByte);
    }

    public static Direction getDirection(int dir)
    {
        switch (dir)
        {
            case 0:
                return Direction.LEFT;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.UP;
            case 3:
                return Direction.DOWN;
            default:
                return Direction.NONE;
        }
    }
}
