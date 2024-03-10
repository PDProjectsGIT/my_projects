.data

    ; Constants read from high-level program

    pictureWidth     dd ?   ; width
    centerX          dd ?   ; center X
    centerY          dd ?   ; center Y
    vignetteRed      dd ?   ; RGB colors
    vignetteGreen    dd ?
    vignetteBlue     dd ?
    maxDistance      dd ?   ; vignette distance (from center)
    vignetteIntensity dd ?  ; vignette intensity

    ; Other program constants

    min_mask         dd 255 ; mask for determining the minimum
    negativeOne      dd -1.0
    one              dd 1.0

.code

; Procedure calculating exp(x)

; variable x in xmm4
; result in xmm4

_exp:

    sub rsp, 96                     ; consider space for data loaded from the high-level program

    movss dword ptr [rsp], xmm4     ; load the value from xmm4 onto the stack

    finit                           ; initialize FPU stack
    fld       dword ptr [rsp]       ; load content from the stack rsp
    fldl2e                          ; load log2(e)
    fmulp st(1),st(0)               ; st0 = x*log2(e) = tmp1
    fld1                            ; load 1
    fscale                          ; st0 = 2^int(tmp1), st1=tmp1
    fxch                            ; swap on the stack (st0 and st1)
    fld1
    fxch                            ; st0 = tmp1, st1=1, st2=2^int(tmp1)
    fprem                           ; st0 = fract(tmp1) = tmp2
    f2xm1                           ; st0 = 2^(tmp2) - 1 = tmp3
    faddp st(1),st(0)               ; st0 = tmp3+1, st1 = 2^int(tmp1)
    fmulp st(1),st(0)               ; st0 = 2^int(tmp1) + 2^fract(tmp1) = 2^(x*log2(e))
    fstp      dword ptr [rsp]       ; result on the rsp stack
    movss  xmm4, dword ptr [rsp]    ; result to xmm4

    add rsp, 96
    
    ret

GenerateASM proc

    ; Copying to variables

    mov     rdi, rcx            ; copy pixelBuffer to rdi, it will be used there
    mov     rbx, rdx            ; copy start index to rbx, it will be used there
    mov     rcx, r8             ; copy end index to rcx, it will be used there
    mov     pictureWidth, r9d   ; copy the rest of the variables (constants)
    mov     eax, [rsp + 40]
    mov     centerX, eax
    mov     eax, [rsp + 48]
    mov     centerY, eax
    mov     eax, [rsp + 56]
    mov     vignetteRed, eax
    mov     eax, [rsp + 64]
    mov     vignetteGreen, eax
    mov     eax, [rsp + 72]
    mov     vignetteBlue, eax
    mov     eax, [rsp + 80]
    mov     maxDistance, eax  
    mov     eax, [rsp + 88]
    mov     vignetteIntensity, eax  


    ; Prepare the main loop
     
    sub     rcx, rbx            ; end of array (end index - start index)
    imul    rbx, 4              ; beginning of array (times 4 because 4 bytes)

    ; Load mask for minimum

    movss    xmm1, min_mask     ; prepare mask for determining minimum
    shufps  xmm1, xmm1, 0       ; fill

    ; Load RGB values into xmm7

    xorps xmm4, xmm4                ; zero xmm4 (here will be stored the vignette factor)

    xorps xmm7, xmm7                ; zero xmm7
    addss  xmm7, [vignetteBlue]
    shufps xmm7, xmm7, 39h          ; shift by one element
    addss  xmm7, [vignetteGreen]
    shufps xmm7, xmm7, 39h          ; shift by one element
    addss xmm7, [vignetteRed]
    shufps xmm7, xmm7, 39h          ; shift by one element
    shufps xmm7, xmm7, 39h          ; shift by one element (initial setting)

    movd xmm6, vignetteIntensity    ; load vignette intensity into xmm6

    movd xmm8, maxDistance          ; load vignette radius into xmm8

    ; Loop processing data batches

ProcessLoop:

    ; Check if there is still data to process

    cmp     rcx, 0                  ; compare the number of elements to process
    jle     EndLoop                 ; if less than or equal, end the loop

    ; Calculate i = 4 k / (16 * pictureWidth) = k / 4 * pictureWidth

    mov     rax,    rbx             ; dividend 4k
    xor     rdx,    rdx             ; zero rdx, remainder
    mov     r8d,    pictureWidth    ; divisor picture width
    imul    r8d,    16
    idiv    r8d                     ; perform signed division             

    CVTSI2SS xmm2, eax              ; result of division in eax, load j into xmm2, convert from int to float

    ; j = (k/4) % pictureWidth, 4k in rbx
    mov     rax,    rbx             ; dividend 4k
    shr     rax,    4               ; logical shift by 4. So 4k * 1/16 = k/4
    xor     rdx,    rdx             ; zero rdx, remainder
    mov     r8d,    pictureWidth    ; divisor picture width
    idiv    r8d

    CVTSI2SS xmm3, edx              ; result of modulo in edx, load i into xmm3, convert from int to float

    ; Calculate distance
    
    CVTSI2SS xmm4, centerY          
    subss xmm2, xmm4                ; distanceX = i - centerY, convert from int to float

    CVTSI2SS xmm4, centerX
    subss xmm3, xmm4                ; distanceY = j - centerX, convert from int to float

    movdqu xmm4, xmm2
    mulps  xmm4, xmm2               ; xmm4 = distanceX ^ 2

    movdqu xmm5, xmm3
    mulps  xmm5, xmm3               ; xmm5 = distanceY ^ 2

    addss xmm4, xmm5                ; xmm4 = distanceX ^ 2 + distanceY ^ 2

    sqrtss xmm4, xmm4               ; distance = sqrt(distanceX ^ 2 + distanceY ^ 2)

   

 divss xmm4, xmm8                ; distance2 = distance/maxDistance

    movd xmm5, negativeOne          ; load -1 into xmm5

    mulps xmm4, xmm5                ; -distance2

    call _exp                       ; execute exp(-distance2), result in xmm4

    mulss xmm4, xmm6                ; multiply vignetteFactor by intensity

    shufps xmm4, xmm4, 00000011b    ; Set the same value everywhere except the last byte, where it remains 0 (alpha channel)

    addss xmm4, [one]               ; 1 for the alpha channel, multiplication leaves it unchanged

    shufps xmm4, xmm4, 39h          ; shift by one position, alpha channel at k+3

    ; Data operations

    movdqu xmm0, [rdi+rbx]          ; load 4 bytes from the array into xmm0
   
    mulps xmm0, xmm4                ; multiply by vignetteFactor

    addps xmm0, xmm7                ; add color correction values
    
    minps xmm0, xmm1                ; set minimum values for channels (255) using the mask

    movdqu [rdi+rbx], xmm0          ; save back to the array

    ; Move the index and decrement the loop counter

    add     rbx, 16                 ; move the pointer by 16 (4 bytes = 4 * 4 = 16)
    sub     rcx, 4                  ; processed 4 elements

    ; Return to the beginning of the loop

    jmp     ProcessLoop             


EndLoop:

    ret                             ; end the procedure

GenerateASM endp
end