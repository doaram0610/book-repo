import React from 'react';
import { Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const BorrowItem = (props) => {
  const { borrowId, userId, bookId, title, author, startDate, endDate } =
    props.borrowbook;
  return (
    <tr>
      <td className="text-center">{borrowId}</td>
      <td>
        {title}
        {/* <Link to={'/book/' + bookId}>{title}</Link> */}
      </td>
      <td className="text-center">{author}</td>
      <td className="text-center">{startDate.substring(0, 10)}</td>
      <td className="text-center">{endDate.substring(0, 10)}</td>
      <td className="text-center">
        <Form.Check name="check_borrow" value={borrowId} />
      </td>
    </tr>
  );
};

export default BorrowItem;
