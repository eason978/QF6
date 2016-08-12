/*
 * Modbus.java
 */

/* 
 * The jModbus project is distrubuted under the following license terms
 * 
 * Copyright (c) 2001 by The Java Modbus Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 *  1.  Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the following disclaimer. 
 *  2.  Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in 
 *      the documentation and/or other materials provided with the 
 *      distribution. 
 *  3.  Neither the name of the The Java Modbus Project nor the names of 
 *      its contributors may be used to endorse or promote products 
 *      derived from this software without specific prior written 
 *      permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY 
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.example.rs485_demo;

public class Modbus {

    public static final int debug = 1;

    public static final byte READ_COIL_STATUS               = (byte) 0x01;

    public static final byte READ_INPUT_STATUS    = (byte) 0x02;

    public static final byte READ_HOLDING_REGISTERS  = (byte) 0x03;

    public static final byte READ_INPUT_REGISTERS     = (byte) 0x04;

    public static final byte FORCE_SINGLE_COIL     = (byte) 0x05;

    public static final byte PRESET_SINGLE_REGISTER    = (byte) 0x06;

    public static final byte READ_EXCEPTION_STATUS    = (byte) 0x07;

    public static final byte DIAGNOSTICS_QUERY   = (byte) 0x08;

    public static final byte FETCH_COMM_EVENT_COUNTER    = (byte) 0x0B;

    public static final byte FETCH_COMM_EVENT_LOG    = (byte) 0x0C;

    public static final byte FORCE_MULTIPLE_COILS    = (byte) 0x0F;

    public static final byte PRESET_MULTIPLE_REGISTERS = (byte) 0x10;

    public static final byte REPORT_SLAVE_ID   = (byte) 0x11;

    public static final byte READ_GENERAL_REFERENCE  = (byte) 0x14;

    public static final byte WRITE_GENERAL_REFERENCE  = (byte) 0x15;

    public static final byte MASK_WRITE_4X_REGISTER  = (byte) 0x16;

    public static final byte READ_WRITE_4X_REGISTERS  = (byte) 0x17;

    public static final byte READ_FIFO_QUEUE  = (byte) 0x18;


    public static final byte EXCEPTION_MODIFIER       = (byte) 0x80;

    public static final byte ILLEGAL_FUNCTION         = (byte) 0x01;

    public static final byte ILLEGAL_DATA_ADDRESS     = (byte) 0x02;

    public static final byte ILLEGAL_DATA_VALUE       = (byte) 0x03;

    public static final byte ILLEGAL_RESPONSE_LENGTH  = (byte) 0x04;

    public static final int ADDRESS_MAX            = 65535;		

    public static final int MAX_MESSAGE_LENGTH = 256;

    public static int UINT16_MAX = (int) 0xFFFF;

    public static int UINT16_MIN = (int) 0x0000;

    public static int UINT8_MAX = (int) 0xFF;

    public static int UINT8_MIN = (int) 0x00;


}




































































