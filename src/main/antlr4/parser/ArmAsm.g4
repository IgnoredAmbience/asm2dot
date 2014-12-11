grammar ArmAsm;

progr : (directive | label | instr)+;

directive : '.word' NUM
          | '.ascii' STR 
          | '.global' ID
          | '.' ID
          ;


instr : instrL reglist					# instrOther
      | instrS operand (',' operand)+	# instrOther
      | instrB ID						# instrBranch
      ;
      
instrL : 'PUSH' | 'POP';
instrS : 'MOV' | 'MOVNE' | 'LDR' | 'LDRSB' | 'STR' | 'STRB' | 'ADD' | 'SUB' | 'CMP' | 'MOVEQ';
instrB : 'B' | 'BL' | 'BEQ';
        
operand : register
        | constant
        | label_expr
        | offset
        ;
        
label_expr : '=' (ID | NUM);
           
reglist : '{' register (',' register)* '}' ;

register : REG_R
         | 'sp' | 'SP'
         | 'lr' | 'LR'
         | 'pc' | 'PC'
         ;
offset : '[' register (',' constant)? ']' ;

constant : '#' (NUM | CHR);

label : ID ':';

REG_R: ('r'|'R') NUM;
ID : [a-zA-Z][a-zA-Z0-9_]* ;             // match lower-case identifiers
NUM : [0-9]+ ;

STR : '"' (~'"' | '\\"')* '"' ;
CHR : '\'' (~'\'' | '\\\'') '\'' ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

