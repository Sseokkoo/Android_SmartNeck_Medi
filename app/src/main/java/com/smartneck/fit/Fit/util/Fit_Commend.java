package com.smartneck.fit.Fit.util;

public class Fit_Commend {

    private final byte STX_1 = 68;
    private final byte STX_2 = 59;
    private final byte SEAT_03 = 3;
    private final byte WEIGHT_04 = 4;
    private final byte GO_EXERCISE_07 = 7;


    public Fit_Commend() {
    }

    public byte[] sendSeatMove(byte position) {

        byte[] commend = new byte[4];

        commend[0] = STX_1;
        commend[1] = STX_2;
        commend[2] = SEAT_03;
        commend[3] = position;


        return getCheckSum(commend);

    }

    public byte[] sendWeightMove(byte position) {

        byte[] commend = new byte[4];

        commend[0] = STX_1;
        commend[1] = STX_2;
        commend[2] = WEIGHT_04;
        commend[3] = position;


        return getCheckSum(commend);

    }

    public byte[] sendGoExercise(byte seat, byte weight){
//                    setMessage(StringUtils.getCommand("44 3A 07 00 03 " + StringUtils.getHexStringCode(Preset.seat) + " 00 00 00 00 00 00 00 00 00 00 " + StringUtils.getHexStringCode(Preset.setup) + " 00 00"));

        byte[] commend = new byte[19];

        commend[0] = STX_1;
        commend[1] = STX_2;
        commend[2] = GO_EXERCISE_07;
        commend[3] = 0;
        commend[4] = 3;//back
        commend[5] = seat;
        commend[6] = 0;
        commend[7] = 0;
        commend[8] = 0;
        commend[9] = 0;
        commend[10] = 0;
        commend[11] = 0;
        commend[12] = 0;
        commend[13] = 0;
        commend[14] = 0;
        commend[15] = 0;
        commend[16] = weight;
        commend[17] = 0;
        commend[18] = 0;

        return getCheckSum(commend);

    }

    private byte[] getCheckSum(byte[] commend){

        int arraySize = commend.length + 1;

        byte[] returnArray = new byte[arraySize];
        byte checkSum = 0;
        for (int i = 0; i < arraySize; i++){


            if (i < arraySize-1){
                returnArray[i] = commend[i];
                checkSum ^= commend[i];
            }else{
                returnArray[i] = checkSum;
            }

        }


        return returnArray;


    }

}
