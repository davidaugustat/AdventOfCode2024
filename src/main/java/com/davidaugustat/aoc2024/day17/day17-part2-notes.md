# Notes on Day 17, Part 2
## Program
My given program:
```
2,4: B = A % 8
1,5: B = B xor 5
7,5: C = A / 2^B
1,6: B = B xor 6
0,3: A = A / 2^3
4,6: B = B xor C
5,5: print(B % 8)
3,0: if A !=0 goto 0
```

## Comments:
- Each iteration prints out exactly one digit. As the program has 16 digits, 16 iterations of the program are required.
- In each iteration, the lowest 3 bits of `A` are cut off.
- In each iteration we can represent the calculation of `B` as follows:
    ```
    B = ((((A % 8) xor 5) xor 6) xor (A / 2^((A % 8) xor 5))) % 8
      = ((((A & 7) ^ 5) ^ 6) ^ (A >> ((A & 7) ^ 5))) & 7
    ```
- As we can see, only the lowest 10 bits of the current `A` are relevant.
- Thus, in iteration `i` we need to find a 10-bit number such that `B` evaluates to the `i`-th digit of the program (from left to right).
  - As $2^{10} = 1024$ is small, we can just brute-force this, leaving us with $16\cdot 2^{10} = 16384$ evaluations of `B`.
  - For each of the 16 digits this gives us a set of 10-bit candidates.
  - The candidates of two consecutive iterations overlap in 7 bits, as they are 10 bits each and in each iteration the lowest 3 bits are cut off `A`.
- We then need to find 16 candidates (one from each iteration) such that for each pair of candidates from consecutive iterations the 7 overlapping bits have the same values.
- When we combine these 16 candidates, we get a number with $3\cdot 16 + 7 = 55$ bits. This number is the value for `A` such that the program outputs itself.
- As the question is to find the *smallest* A such that the program outputs itself, we need to find all possible sets of 16 candidates an not only one set. Then take the minimum.
