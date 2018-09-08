# Recover Rover

## Processing reviews

Chose opencsv library for its versatility and simplicity.
Supports streaming input, so we don't overload the memory with file data all at once.
 
### Assumptions
1. Sitter's info does not vary by properties. 
   When reading the review entries, we consider it to be the same sitter if all of the following match:
    - name
    - phone
    - email
    - image
