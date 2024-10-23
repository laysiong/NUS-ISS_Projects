import React from 'react';
import { ListGroup, Button, Row, Col } from 'react-bootstrap';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

const UserList = ({ currentUser, users, onToggleFollow, onBlock, onUnblock, isUserFollowing }) => {
    return (
        <ListGroup>
            {users.length === 0 ? (
                <>No users yet.</>
            ) : (
                <>
                    {users.map(user => {
                        const following = isUserFollowing ? isUserFollowing(user.id) : false;

                        return (
                            <ListGroup.Item key={user.id}>
                                <Row className="align-items-center">
                                    <Col xs={6}>
                                        <Link to={`/userProfile/${user.id}`}>{user.name}</Link>
                                        <div>{user.username}</div>
                                    </Col>
                                    <Col xs={6} className="d-flex justify-content-end">
                                        {onToggleFollow && !user.isBlocked && (
                                            <Button
                                                variant={following ? 'danger' : 'primary'}
                                                onClick={() => onToggleFollow(user.id)}
                                                className="mr-2"
                                            >
                                                {following ? 'Unfollow' : 'Follow'}
                                            </Button>
                                        )}
                                        {onBlock && !user.isBlocked && (
                                            <Button
                                                variant="warning"
                                                onClick={() => onBlock(currentUser,user.id)}
                                            >
                                                Block
                                            </Button>
                                        )}
                                        {onUnblock && (
                                            <Button
                                                variant="secondary"
                                                onClick={() => onUnblock(currentUser,user.id)}
                                            >
                                                Unblock
                                            </Button>
                                        )}
                                    </Col>
                                </Row>
                            </ListGroup.Item>
                        );
                    })}
                </>
            )}
        </ListGroup>
    );
};

// UserList.propTypes = {
//     users: PropTypes.arrayOf(
//         PropTypes.shape({
//             id: PropTypes.number.isRequired,
//             username: PropTypes.string.isRequired,
//             name: PropTypes.string.isRequired,
//             isFollowing: PropTypes.bool,
//             isBlocked: PropTypes.bool,
//         })
//     ).isRequired,
//     onToggleFollow: PropTypes.func,
//     onBlock: PropTypes.func,
//     onUnblock: PropTypes.func,
//     isUserFollowing: PropTypes.func,
// };

export default UserList;
