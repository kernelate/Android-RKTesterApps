#ifndef __LED_DRIVERS_H__
#define __LED_DRIVERS_H__

typedef struct {
	int special_function_register;
	int value;
} led_data;

// SFR OPERATION
#define ON_OPS						1
#define ON_DELAY_OPS				2
#define SLOW_OPS	    			3
#define	 FAST_OPS					4
#define	 ONCE_OPS					5
#define	 TWICE_OPS					6
#define	 RECOVERY_OPS				7
#define	 OFF_OPS 					8

#define ON					_IOW('p', 1, led_data)
#define ON_DELAY			_IOW('p', 2, led_data)
#define SLOW	 			_IOW('p', 3, led_data)
#define FAST				_IOW('p', 4, led_data)
#define ONCE		    	_IOW('p', 5, led_data)
#define TWICE		    	_IOW('p', 6, led_data)
#define RECOVERY	    	_IOW('p', 7, led_data)
#define OFF					_IOW('p', 8, led_data)


#endif //__LED_DRIVERS_H__
