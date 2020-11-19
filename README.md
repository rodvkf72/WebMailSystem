# WebMailSystem
JSP를 활용한 웹 메일 시스템 유지/보수 입니다.  
유지/보수 유형별 구현은 다른 유형과의 차이를 서술하기 위한 것이지 다른 유형에서 사용한 기법 및 기술을 사용하지 않은 것이 아닙니다.  
<br>
<br>


| 역할 | 학과 | 학번 | 이름 |
| :- | - | :-: | -: |
| 팀 장 | 소프트웨어공학과 | 20143226 | 김광호 |
| 팀원1 | 소프트웨어공학과 | 20173114 | 권미소 |
| 팀원2 | 소프트웨어공학과 | 20143221 | 김민수 |
| 팀원3 | 소프트웨어공학과 | 20173237 | 김희정 |
<br>
<br>
<br>


## 개요
본 프로젝트는 '객체지향설계' 수업에서 학습한 JSP 웹 개발 방법을 기반으로 웹 메일 프로그램을 개발하고 유지/보수를 통해 더 향상된 프로그램을 만드는 것을 목표로 한다.  
<br>
<br>
<br>


## 기대효과

- 개인의 기술적 능력 향상
- github를 이용한 팀원 간의 형상관리 기법 수행
- SMTP, POP3, telnet 등 프로토콜의 원리
- Java와 MySQL의 연동 및 slf4j 로그 사용법
- 스크럼 기법을 통한 요구사항 위주의 개발 경험
<br>
<br>
<br>


## 유지/보수 유형별 구현
<br>
<br>


####  1. 교정 유지보수  
교정 유지보수를 위해 정적 분석 도구인 Yasca, SonarQube를 사용하였습니다.  
프로젝트를 진행하면서 정적 분석을 수행하고 오류 중 해결이 필요한 문제는 분석하고 수정하였습니다.  
<br>


<center>  ![image](https://user-images.githubusercontent.com/48707324/99666621-8e006a00-2aae-11eb-825e-2590a14728ee.png)</center>  
<center>  ▲ Yasca 사용 모습</center>  
<br>


<center>![image](https://user-images.githubusercontent.com/48707324/99666662-9ce71c80-2aae-11eb-80fc-1d46159e1665.png)</center>  
<center>▲ SonarQube 사용 모습</center>  
<br>
<br>
<br>


####  2. 적응 유지보수  
적응 유지보수를 위해 코드를 분석하였습니다.  
기존의 첨부파일 추가 기능은 첨부파일이 여러개 일 경우 메인 쓰레드가 순차적으로 동작하게 되어 첨부파일 추가 시간이 오래걸릴 수 있었습니다.
따라서 유스케이스 다이어그램을 그리고 본문을 분석하여 시스템 시퀀스 다이어그램, 도메인 모델, 통신 다이어그램, 설계 클래스 다이어그램을 작성하고 코드를 수정하였습니다.  
<br>


<center>![image](https://user-images.githubusercontent.com/48707324/99668330-eb95b600-2ab0-11eb-9091-a93ea9b3abe6.png)</center>  
<center>▲ 본문 문석</center>  
<br>


<center>![image](https://user-images.githubusercontent.com/48707324/99668420-0d8f3880-2ab1-11eb-97d8-40c6e6b26816.png)</center>  
<center>▲ 시스템 시퀀스 다이어그램</center>  
<br>
<br>
<br>


####  3. 완전화 유지보수  
완전화 유지보수를 위해 시스템 테스트 케이스 작성하였습니다.  
<br>


<center>![image](https://user-images.githubusercontent.com/48707324/99669412-5c899d80-2ab2-11eb-97de-c5e6b394426c.png)</center>  
<center>▲ 전체적인 시스템 테스트 케이스</center>  
<br>


<center>![image](https://user-images.githubusercontent.com/48707324/99669522-893db500-2ab2-11eb-8ee3-f8bf8de54e17.png)</center>  
<center>▲ 시스템 테스트 케이스의 일부</center>  
<br>
<br>
<br>


####  4. 예방 유지보수  
예방 유지보수를 위해 보안을 강화하였습니다.  
안전한 비밀번호 예시 (http://www.strongpasswordgenerator.com) 를 참고하였으며 yasca 분석을 통한 오류 검출, 에러 페이지 생성, 비밀번호 변경 요청, XSS 공격 방지를 위한 XSS 필터 구현 등을 하였습니다.  
<br>


<center>![image](https://user-images.githubusercontent.com/48707324/99670543-0ddd0300-2ab4-11eb-9bb7-ed66ffdb7de2.png)</center>  
<center>▲ 안전한 비밀번호 예시</center>  
<br>


<center>![image](https://user-images.githubusercontent.com/48707324/99670788-6b714f80-2ab4-11eb-9cd5-f78b9d47cf84.png)</center>  
<center>▲ 에러 페이지 설계</center>  
<br>
<br>
<br>


## 일정 계획  
<br>


- WBS  
<center>![image](https://user-images.githubusercontent.com/48707324/99670921-a1163880-2ab4-11eb-9afc-c99704cf647b.png)</center>  
<br>


- 최소 소요기간 산정  
<center>![image](https://user-images.githubusercontent.com/48707324/99670954-ad01fa80-2ab4-11eb-8a54-878d02ff816a.png)</center>  
<br>


- 간트 차트  
<center>![image](https://user-images.githubusercontent.com/48707324/99670984-b8edbc80-2ab4-11eb-9f92-18cbba2990a7.png)</center>  
<br>
<br>
<br>


## 형상관리 / 협업도구  
<br>

- Git  
<center>![image](https://user-images.githubusercontent.com/48707324/99671106-eaff1e80-2ab4-11eb-8b16-d23115e4af94.png)</center>  
<br>


- Notion  
<center>![image](https://user-images.githubusercontent.com/48707324/99671198-0b2edd80-2ab5-11eb-9910-202e8b43bd14.png)</center>  
