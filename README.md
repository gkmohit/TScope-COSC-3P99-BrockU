# TScope: Automatic Timeout Bug Identification for Server Systems
# Table of Contents
1. [Introduction](#introduction)
2. [Original Project](#original-project)
   1. [GitHub URL](#github-url)
   2. [Link to Paper](#link-to-paper)
   3. [Authors](#authors)
3. [Summary of Changes Made to Project](#summary-of-changes-made-to-project)

## Introduction
Timeout is commonly used to handle unexpected failures in server systems. 
However, improper use of timeout can cause server systems to hang or experience 
performance degradation. In this paper, we present TScope, an automatic timeout 
bug identification tool for server systems. TScope leverages kernel-level system 
call tracing and machine learning based anomaly detection and feature extraction 
schemes to achieve timeout bug identification. TScope introduces a unique system 
call selection scheme to achieve higher accuracy than existing generic performance 
bug detection tools. We have implemented a prototype of TScope and conducted 
extensive experiments using 19 real-world server performance bugs, including 12 
timeout bugs and 7 non-timeout performance bugs. The experimental results show 
that TScope correctly classifies 18 out of 19 bugs. Compared to existing runtime 
bug detection schemes, TScope reduces the average false positive rate from 
47.24% to 0.8%. TScope is light-weight and does not require application 
instrumentation, which makes it practical for production server performance 
bug identification.


---
## Original Project
### GitHub URL
The original TScope was built by North Carolina State University DANCE Research group.
The repository can be found [here](https://github.com/NCSU-DANCE-Research-Group/TScope).
### Link to Paper

### Authors
- Jingzhu He
- Ting Dai
- Xiaohui Gu
----
## Summary of Changes Made to Project
- Organized classes into respective packages for easy readying
- Extracted all hard coded list of files into a utility file for easy editing
----