package com.seamfix.seamfixassessment.algorithm;

import java.util.Arrays;
import java.util.List;

public class NotificationAlgorithm {
//    public static void main(String[] args) {
//
//        int [] range  = {2, 3, 4, 2, 3, 6 ,8 ,4 ,5};
//        System.out.println(notificationCounter(9, 5, range));
//    }
    public int notificationCounter(int dataLength, int dataCheckRange, int[]dataList){

        int notification =  0;
        for (int i = 0; i < dataList.length; i++) {
            if(i >= dataCheckRange){
                int valToCompare = dataList[i];
                int[] croppedData = transactionData(i - dataCheckRange, dataCheckRange, dataList);
                Arrays.sort(croppedData);
                int median = getMedian(croppedData);

                if(valToCompare >= (2 * median)) {
                    notification++;
                }
            }
        }

        return notification;
    }

    public int[] transactionData(int startCropIndex, int dataRange, int[] dataArrayToBeCropped){
        int[] newData = new int[dataRange];

        int count = startCropIndex;
        for (int x = 0; x < newData.length; x++){
            newData[x] = dataArrayToBeCropped[count];
            count++;
        }

        return newData;
    }

    public int getMedian(int[] dataArray) {
        int median;

        int size = dataArray.length;
        if (size % 2 == 1){
            median = dataArray[((size+1)/2) - 1];
        } else {
            median = (dataArray[(size/2)-1] + dataArray[size/2]) / 2;
        }

        return median;
    }


}
