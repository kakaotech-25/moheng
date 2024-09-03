import { useState, useEffect } from 'react';
import './Mypage.css';
import LivingBtn from '../../components/LivingBtn/LivingBtn';
import Button from '../../components/Button/Button';
import UserForm from "../../components/UserForm/UserForm";
import UserData from "../../data/UserData";

const Mypage = () => {
  const [activeTab, setActiveTab] = useState('tab1');
  const [input, setInput] = useState({
    name: "",
    birth: "",
    gender: "",
  });

  useEffect(() => {
    setInput({
      name: UserData.nickname,
      birth: UserData.birthday,
      gender: UserData.genderType,
    });
  }, []);

  const onChange = (e) => {
    setInput({
      ...input,
      [e.target.name]: e.target.value,
    });
  };

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  const handleLivingSelectionChange = (selectedOptions) => {
    console.log("Selected options:", selectedOptions);
  };

  return (
    <div className="my-page">
      <section className="tabs">
        <button
          className={activeTab === 'tab1' ? 'tab-button active' : 'tab-button'}
          onClick={() => handleTabClick('tab1')}
        >
          프로필
        </button>
        <button
          className={activeTab === 'tab2' ? 'tab-button active' : 'tab-button'}
          onClick={() => handleTabClick('tab2')}
        >
          생활
        </button>
      </section>

      <section className="tab-content">
        {activeTab === 'tab1' && (
          <div id="tab1">
            <section className="mypage-img">
              <img src={UserData.profileImageUrl} className="user-img" alt="User Profile" />
            </section>
            <UserForm input={input} onChange={onChange} />
            <section className="mypage-btn">
              <Button text={"저장"} />
            </section>
          </div>
        )}
        {activeTab === 'tab2' && (
          <div id="tab2" className='mypage-living'>
            <LivingBtn onChangeSelection={handleLivingSelectionChange} />
            <section>
              <Button text={"저장"} />
            </section>
          </div>
        )}
      </section>
    </div>
  );
}

export default Mypage;
