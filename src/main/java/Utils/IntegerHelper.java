package Utils;

public class IntegerHelper {
	
	public static int getTrueDigitCount(int number) {
		return
				number==0?0:number>0?number<100?number<10?1:2:number<1000000?number<10000?number<1000?3:4:number<100000?5:6:number<100000000?number<10000000?7:8:number<1000000000
				?9:10:number>-100?number>-10?1:2:number>-1000000?number>-10000?number>-1000?3:4:number>-100000?5:6:number>-100000000?number>-10000000?7:8:number>-1000000000?9:10;
	}
	
	public static int getDisplayDigitCount(int number) {
		if(number == 0) return 1;                            // Because 0 is most often displayed as "0"
		if(number < 0) return getTrueDigitCount(number) + 1; // Because negative numbers are most often displayed as "-number"
		else return getTrueDigitCount(number);               // This should be obvious.
	}
	
	/* EXPLANATION: Ternary Operators to get code length down, below is the indented version.
	
	|                             |                             | 1
	|                       |     |     |                       | 2
	|           |           |  |  |  |  |           |           | 3
	|     |     |     |     |  |  |  |  |     |     |     |     | 4
	|  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | 5
	 10 09 08 07 06 05 04 03 02 01 01 02 03 04 05 06 07 08 09 10
	
	number > 0 ?                     // 1: +|-
	 number < 100 ?                  //  2: 2|3
	  number < 10 ? 1 : 2            //   3: 1|2
	: number < 1000000 ?             //   3: 6|7
	  number < 10000 ?               //    4: 4|5
		number < 1000 ? 3:4          //     5: 3|4
	  : number < 100000 ? 5:6        //     5: 5|6
	 : number < 100000000 ?          //    4: 8|9
	    number < 10000000 ? 7:8      //     5: 7|8
	  : number < 1000000000 ? 9:10   //     5: 9|10
	:number > -100 ?                 //  2: 2|3
	  number > -10 ? 1:2             //   3: 1|2 E
	: number > -1000000 ?            //   3: 6|7
	   number > -10000 ?             //    4: 4|5
	    number > -1000 ? 3:4         //     5: 3|4
	  : number > -100000 ? 5:6       //     5: 5|6
	 : number > -100000000 ?         //    4: 8|9
	    number > -10000000 ? 7:8     //     5: 7|8
	  : number > -1000000000 ? 9:10; //     5: 9|10
	
	 */
	
}
