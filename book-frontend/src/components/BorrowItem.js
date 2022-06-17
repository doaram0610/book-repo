import React, { useEffect, useState } from 'react';
import { Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';

//isAllChecked, borrowbook, checkedBorrowHandler
const BorrowItem = (props) => {
  const { borrowId, userId, bookId, title, author, startDate, endDate } =
    props.borrowbook;
  const [bChecked, setChecked] = useState(false);

  //체크박스의 선택여부 변경
  const allCheckHandler = () => {
    console.log('allCheckHandler :' + props.isAllChecked);
    setChecked(props.isAllChecked); //전체선택
  };

  //체크박스 선택시 호출
  const checkHandler = ({ target }) => {
    setChecked(!bChecked);
    props.checkedBorrowHandler(borrowId, target.checked);
  };

  useEffect(() => allCheckHandler(), [props.isAllChecked]);

  console.log('isAllChecked: ' + props.isAllChecked);
  // console.log('borrowbook: ' + props.borrowbook);
  // console.log('checkedBorrowHandler: ' + props.checkedBorrowHandler);

  return (
    <tr>
      <td className="text-center">{props.index}</td>
      <td>
        {title}
        {/* <Link to={'/book/' + bookId}>{title}</Link> */}
      </td>
      <td className="text-center">{author}</td>
      <td className="text-center">{startDate.substring(0, 10)}</td>
      <td className="text-center">{endDate.substring(0, 10)}</td>
      <td className="text-center">
        <Form.Check
          name="check_borrow"
          checked={bChecked}
          onChange={(e) => checkHandler(e)}
        />
      </td>
    </tr>
  );
};

export default BorrowItem;
