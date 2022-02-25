package com.seamfix.seamfixassessment.bvnTests;

import com.seamfix.seamfixassessment.algorithm.NotificationAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class NotificationAlgorithmTest {

    NotificationAlgorithm notificationAlgorithm = null;

    @BeforeEach
    void setup() {
        notificationAlgorithm = new NotificationAlgorithm();
    }

    @Test
    public void MedianFunctionTest(){
        int[] oddArray = new int[]{2,3,4};
        int[] evenArray = new int[]{2,2,2,5};

        int oddMedian = notificationAlgorithm.getMedian(oddArray);
        int evenMedian = notificationAlgorithm.getMedian(evenArray);

        assertEquals(3, oddMedian);
        assertEquals(2, evenMedian);
    }

    @Test
    public void DataRangeFunctionTest(){
        int[] array = new int[]{2,2,2,5,6,1,7,1};

        int[] croppedData = notificationAlgorithm.transactionData(1, 6, array);

        assertEquals(6, croppedData.length);

    }

    @Test
    public void NotificationAlgoTest(){
        int[] dataArray = new int[]{2, 3, 4, 2, 3, 6, 8, 4, 5};
        int dataCheckRange = 5;

        int notifications = notificationAlgorithm.notificationCounter(dataArray.length, dataCheckRange, dataArray);

        assertEquals(2, notifications);
    }
}
