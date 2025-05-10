package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class DeliveryNotFoundException extends BusinessBaseException {
    
    
    public DeliveryNotFoundException() {super(ErrorCode.DELIVERY_NOT_FOUND);}
    
    public DeliveryNotFoundException(String message) {super(message, ErrorCode.DELIVERY_NOT_FOUND);}





}//end of class...
