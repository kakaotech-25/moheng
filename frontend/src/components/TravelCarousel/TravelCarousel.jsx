import { useState } from 'react';
import TravelCard from '../TravelCard/TravelCard';
import './TravelCarousel.css';

const TravelCarousel = ({ cards }) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const cardsToShow = 5;

  const handlePrevClick = () => {
    if (currentIndex === 0) {
      setCurrentIndex(cards.length - cardsToShow);
    } else {
      setCurrentIndex(currentIndex - 1);
    }
  };

  const handleNextClick = () => {
    if (currentIndex === cards.length - cardsToShow) {
      setCurrentIndex(0);
    } else {
      setCurrentIndex(currentIndex + 1);
    }
  };

  return (
    <div className="carousel-container">
      <button className="carousel-button prev" onClick={handlePrevClick}>
        ◀
      </button>
      <div className="carousel">
        <div
          className="carousel-track"
          style={{ transform: `translateX(-${currentIndex * (100 / cardsToShow)}%)` }}
        >
          {cards.map((card, index) => (
            <div key={index} className="carousel-card">
              <TravelCard {...card} isSelected={false} onClick={() => console.log(`Clicked on ${card.title}`)} />
            </div>
          ))}
        </div>
      </div>
      <button className="carousel-button next" onClick={handleNextClick}>
        ▶
      </button>
    </div>
  );
};

export default TravelCarousel;