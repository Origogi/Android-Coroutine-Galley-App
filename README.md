# Android-Coroutine-Gallery-App

코루틴을 사용하여 MVC, MVP, MVVM 디자인 패턴을 적용하고 구현 함으로써 디자인 패턴을 학습하기 위한 Sample Code 입니다.

각 디자인 패턴을 적용한 코드는 각각 브랜치로 분기가 되어 있습니다. 각 디자인 패턴에 해당하는 코드를 보기 위해서 아래 가이드를 확인 바랍니다.

## 실행 화면

각 디자인 패턴마다 실행화면은 아래와 같이 모두 동일합니다.

<div align="center">
<img src="https://user-images.githubusercontent.com/35194820/117981811-5e049600-b370-11eb-97d4-52fca92cf356.gif" >
</div>
</br>

앱의 Sequence 는 다음과 같습니다.

1. 서버로 부터 HTML 문서를 다운로드
2. HTML 문서로 부터 Image Url, Image Title 를 다운로드 파싱한다.
3. 파싱한 데이터를 RecyclerView 에 업데이트한다.
4. 파싱한 Item 의 갯수를 카운팅하여 TextView 에 업데이트 한다.

## MVC

~~~bash
git checkout mvc
~~~

## MVP

~~~bash
git checkout mvp
~~~

## MVVM (No databinding)

~~~bash
git checkout mvvm-livedata
~~~

## MVVM (databinding)

~~~bash
git checkout mvvm-databiding
~~~