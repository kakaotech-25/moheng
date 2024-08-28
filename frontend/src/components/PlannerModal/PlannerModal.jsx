import { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import './PlannerModal.css';

const PlannerModal = ({ isOpen, title, plan, onClose, onSave }) => {
  const [currentPlan, setCurrentPlan] = useState(plan || { title: '', period: '' });
  const [selectedDates, setSelectedDates] = useState([null, null]);
  const [startDate, endDate] = selectedDates;

  const formatDate = (date) => {
    return date ? date.toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' }).replace(/\./g, '').replace(/\s/g, '.') : '';
  };

  const handleSave = () => {
    if (startDate && endDate) {
      const period = `${formatDate(startDate)} ~ ${formatDate(endDate)}`;
      onSave({ ...currentPlan, period });
      onClose();
    }
  };

  return (
    isOpen && (
      <div className="modal-overlay">
        <div className="modal-content">
          <div className="modal-header">
            <h2>{title}</h2>
          </div>
          <div className="modal-body">
            <div className="modal-input-group">
              <label>일정 이름</label>
              <input
                type="text"
                name="title"
                value={currentPlan.title}
                onChange={(e) => setCurrentPlan({ ...currentPlan, title: e.target.value })}
              />
            </div>
            <div className="modal-input-group">
              <label>일정 기간</label>
              <input
                type="text"
                name="period"
                value={
                  startDate && endDate
                    ? `${formatDate(startDate)} ~ ${formatDate(endDate)}`
                    : ''
                }
                readOnly
              />
            </div>
            <div className="calendar-container">
              <DatePicker
                selected={startDate}
                onChange={(dates) => setSelectedDates(dates)}
                startDate={startDate}
                endDate={endDate}
                selectsRange
                inline
                dateFormat="yyyy.MM.dd"
              />
            </div>
          </div>
          <div className="modal-footer">
            <button className="save-button" onClick={handleSave}>저장</button>
            <button className="cancel-button" onClick={onClose}>취소</button>
          </div>
        </div>
      </div>
    )
  );
};

export default PlannerModal;
