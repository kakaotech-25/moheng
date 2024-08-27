const PlannerData = [
    {
      id: 1,
      title: "서울 투어",
      period: "2024-09-01 ~ 2024-09-05",
      destinations: ["롯데월드", "경복궁", "남산타워", "명동", "동대문시장"],
    },
    {
      id: 2,
      title: "경기도 가족 여행",
      period: "2024-09-10 ~ 2024-09-12",
      destinations: ["에버랜드", "수원 화성", "한국민속촌", "용인 대장금파크"],
    },
    {
      id: 3,
      title: "강원도 자연 휴양",
      period: "2024-09-15 ~ 2024-09-20",
      destinations: ["남이섬", "설악산", "속초 해수욕장", "오대산", "평창 양떼목장"],
    },
    {
      id: 4,
      title: "부산 힐링 여행",
      period: "2024-09-25 ~ 2024-09-28",
      destinations: ["해운대", "광안리", "태종대", "자갈치시장", "부산타워"],
    },
    {
      id: 5,
      title: "제주도 완전정복",
      period: "2024-10-01 ~ 2024-10-07",
      destinations: ["한라산", "성산일출봉", "섭지코지", "우도", "협재해수욕장"],
    },
    {
      id: 6,
      title: "서울 역사 탐방",
      period: "2024-10-10 ~ 2024-10-13",
      destinations: ["경복궁", "창덕궁", "종묘", "덕수궁", "국립중앙박물관"],
    },
    {
      id: 7,
      title: "경상도 맛집 여행",
      period: "2024-10-15 ~ 2024-10-18",
      destinations: ["경주 황리단길", "대구 서문시장", "부산 국제시장", "울산 간절곶", "포항 호미곶"],
    },
    {
      id: 8,
      title: "전라도 문화 탐방",
      period: "2024-10-20 ~ 2024-10-23",
      destinations: ["전주 한옥마을", "순천만 국가정원", "담양 메타세쿼이아 길", "광주 국립박물관", "여수 오동도"],
    },
    {
      id: 9,
      title: "충청도 힐링 여행",
      period: "2024-10-25 ~ 2024-10-28",
      destinations: ["태안 안면도", "서산 마애삼존불", "공주 공산성", "대천 해수욕장", "단양 구인사"],
    },
    {
      id: 10,
      title: "제주도 가을 여행",
      period: "2024-11-01 ~ 2024-11-05",
      destinations: ["한라산", "천지연폭포", "주상절리", "제주 오름", "제주 동문시장"],
    },
    {
      id: 11,
      title: "서울 인근 데이투어",
      period: "2024-11-10 ~ 2024-11-12",
      destinations: ["남산타워", "청계천", "홍대", "이태원", "북촌 한옥마을"],
    },
    {
      id: 12,
      title: "충북 자연 힐링",
      period: "2024-11-15 ~ 2024-11-18",
      destinations: ["속리산", "청주 상당산성", "단양 팔경", "보은 법주사", "제천 의림지"],
    },
    {
      id: 13,
      title: "남해안 드라이브 여행",
      period: "2024-11-20 ~ 2024-11-25",
      destinations: ["통영 동피랑 마을", "남해 독일마을", "여수 밤바다", "사천 바다케이블카", "고성 공룡박물관"],
    },
    {
      id: 14,
      title: "서울 야경 투어",
      period: "2024-12-01 ~ 2024-12-03",
      destinations: ["남산타워", "한강공원", "서울역 고가도로", "63빌딩", "청계천"],
    },
    {
      id: 15,
      title: "부산 먹방 여행",
      period: "2024-12-05 ~ 2024-12-08",
      destinations: ["광안리", "자갈치시장", "국제시장", "해운대", "부산역"],
    },
    {
      id: 16,
      title: "강원도 겨울여행",
      period: "2024-12-15 ~ 2024-12-20",
      destinations: ["설악산", "평창 스키장", "정동진", "대관령", "강릉 커피거리"],
    },
    {
      id: 17,
      title: "서울 성지순례",
      period: "2024-12-25 ~ 2024-12-27",
      destinations: ["명동성당", "약현성당", "천주교 순교성지", "성모성지", "중림동성당"],
    },
    {
      id: 18,
      title: "경기 북부 투어",
      period: "2025-01-02 ~ 2025-01-04",
      destinations: ["파주 헤이리 예술마을", "임진각", "고양 아쿠아리움", "의정부 미술관", "양주 나리공원"],
    },
    {
      id: 19,
      title: "부산 온천 힐링",
      period: "2025-01-10 ~ 2025-01-12",
      destinations: ["부산 동래온천", "해운대 스파랜드", "태종대", "광안리", "부산 타워"],
    },
    {
      id: 20,
      title: "전북 전통문화 여행",
      period: "2025-01-15 ~ 2025-01-18",
      destinations: ["전주 한옥마을", "군산 근대역사박물관", "익산 미륵사지", "부안 채석강", "남원 광한루"],
    },
    {
      id: 21,
      title: "경상도 해안 여행",
      period: "2025-02-01 ~ 2025-02-05",
      destinations: ["포항 영일대 해수욕장", "울산 대왕암공원", "부산 해운대", "경주 감포 해변", "거제 외도"],
    },
    {
      id: 22,
      title: "강원도 봄꽃 여행",
      period: "2025-03-01 ~ 2025-03-05",
      destinations: ["경포대", "삼척 장호항", "춘천 남이섬", "강릉 경포호", "평창 대관령"],
    },
    {
      id: 23,
      title: "서울 봄날 투어",
      period: "2025-03-10 ~ 2025-03-12",
      destinations: ["서울숲", "석촌호수", "여의도 한강공원", "창경궁", "덕수궁 돌담길"],
    },
    {
      id: 24,
      title: "부산 여름 바캉스",
      period: "2025-07-01 ~ 2025-07-05",
      destinations: ["해운대", "광안리", "송도 해수욕장", "태종대", "오륙도"],
    },
    {
      id: 25,
      title: "제주도 여름 여행",
      period: "2025-07-10 ~ 2025-07-15",
      destinations: ["중문 해수욕장", "성산일출봉", "우도", "협재해수욕장", "비자림"],
    },
    {
      id: 26,
      title: "경주 역사 여행",
      period: "2025-08-01 ~ 2025-08-05",
      destinations: ["경주 불국사", "석굴암", "첨성대", "대릉원", "황리단길"],
    },
    {
      id: 27,
      title: "충청도 드라이브 여행",
      period: "2025-08-10 ~ 2025-08-15",
      destinations: ["단양 구인사", "충주호", "보은 법주사", "대청호", "서산 해미읍성"],
    },
    {
      id: 28,
      title: "경기도 문화 체험",
      period: "2025-08-20 ~ 2025-08-22",
      destinations: ["용인 한국민속촌", "수원 화성", "파주 출판도시", "가평 아침고요수목원", "남양주 다산유적지"],
    },
    {
      id: 29,
      title: "제주도 맛집 여행",
      period: "2025-09-01 ~ 2025-09-05",
      destinations: ["동문시장", "중문 해수욕장", "성산일출봉", "오설록 티뮤지엄", "제주 흑돼지거리"],
    },
    {
      id: 30,
      title: "부산 겨울여행",
      period: "2025-12-01 ~ 2025-12-05",
      destinations: ["해운대", "광안리", "부산역", "동백섬", "송도 케이블카"],
    },
    {
      id: 31,
      title: "경상북도 투어",
      period: "2025-10-01 ~ 2025-10-05",
      destinations: ["안동 하회마을", "경주 양동마을", "영덕 대게거리", "포항 죽도시장", "청도 소싸움경기장"],
    },
    {
      id: 32,
      title: "충청남도 문화유산 여행",
      period: "2025-11-01 ~ 2025-11-05",
      destinations: ["공주 공산성", "서산 마애삼존불", "논산 관촉사", "부여 낙화암", "태안 천리포수목원"],
    },
    {
      id: 33,
      title: "강원도 겨울여행",
      period: "2025-12-20 ~ 2025-12-25",
      destinations: ["평창 대관령", "강릉 주문진", "동해 무릉계곡", "춘천 남이섬", "속초 해수욕장"],
    },
    {
      id: 34,
      title: "서울 크리스마스 투어",
      period: "2025-12-23 ~ 2025-12-25",
      destinations: ["명동성당", "남산타워", "청계천", "코엑스몰", "롯데월드"],
    },
    {
      id: 35,
      title: "경상남도 투어",
      period: "2026-01-02 ~ 2026-01-07",
      destinations: ["통영 케이블카", "거제 외도", "사천 바다케이블카", "남해 독일마을", "창원 주남저수지"],
    },
    {
      id: 36,
      title: "전라남도 자연여행",
      period: "2026-02-01 ~ 2026-02-05",
      destinations: ["순천만 국가정원", "여수 밤바다", "담양 메타세쿼이아 길", "곡성 섬진강", "보성 녹차밭"],
    },
    {
      id: 37,
      title: "경기도 겨울 축제",
      period: "2026-02-15 ~ 2026-02-18",
      destinations: ["가평 아침고요수목원", "이천 설봉공원", "파주 출판도시", "광주 남한산성", "양평 두물머리"],
    },
    {
      id: 38,
      title: "제주도 봄 여행",
      period: "2026-03-01 ~ 2026-03-05",
      destinations: ["성산일출봉", "우도", "섭지코지", "한라산", "제주 오름"],
    },
    {
      id: 39,
      title: "전라북도 전통문화 여행",
      period: "2026-04-01 ~ 2026-04-05",
      destinations: ["전주 한옥마을", "부안 채석강", "남원 광한루", "익산 미륵사지", "고창 고인돌유적"],
    },
    {
      id: 40,
      title: "서울 도심 투어",
      period: "2026-05-01 ~ 2026-05-05",
      destinations: ["경복궁", "남산타워", "명동", "동대문디자인플라자", "서울숲"],
    }
  ];
  
  export default PlannerData;
  