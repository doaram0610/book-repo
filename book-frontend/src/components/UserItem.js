import React from 'react';
import { Link } from 'react-router-dom';

const UserItem = (props) => {
  const { userId, userName, provider, providerId, role, update } = props.user;
  return (
    <tr>
      <td>1</td>
      <td>{userId}</td>
      <td>
        <Link to={'/user/' + userId}>{userName}</Link>
      </td>
      <td>{provider}</td>
      <td>{providerId}</td>
      <td>{role}</td>
      <td>{update}</td>
    </tr>
  );
};

export default UserItem;
