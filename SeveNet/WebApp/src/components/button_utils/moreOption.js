// moreOption.js
import React, { useState } from 'react';
import { MoreHoriz as MoreHorizIcon } from '@mui/icons-material';
import PopupState, { bindTrigger, bindMenu } from 'material-ui-popup-state';
import PC_MsgService from '../../services/PC_MsgService';
import { IconButton, Menu, MenuItem, Dialog, DialogTitle, DialogContent, TextField, DialogActions, Button } from '@mui/material';


const handleDelete = async (id) => {
    console.log("Delete " + id);
    try {
        if(id){
            const response = await PC_MsgService.deletePost(id);
            //console.log(`Post ${id} deleted successfully.`);
        }
    } catch (error) {
        console.error('Error deleting post:', error);
    }
};

const handleHide = async (id) => {
    console.log("Hide " + id);
    try {
        const response = await PC_MsgService.hidePost(id);
        //console.log(`Post ${id} hidden successfully.`);
    } catch (error) {
        console.error('Error hiding post:', error);
    }
};

const handleEdit = async (id) => {
    console.log("Edit " + id);
    
};


const MoreOption = ({ id, auth, refreshTags, status}) => {
    const [open, setOpen] = useState(false);
    const [tagData, setTagData] = useState({ tag: '', remark: '' });
    const [loading, setLoading] = useState(false);

    const handleEdit = async (id) => {
        setLoading(true);
        try {
            const response = await PC_MsgService.getTagById(id);
            const tag = response.data;
            setTagData({
                ...tag,
                tag: tag.tag || '', // Ensure non-null value
                remark: tag.remark || '', // Ensure non-null value
            });            
            setOpen(true);
        } catch (error) {
            console.error('Error fetching tag data:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSave = async () => {
        try {
            await PC_MsgService.updateTag(tagData.id, tagData);
            setOpen(false);
            console.log("moreOption save");
            refreshTags(); // Trigger refresh in parent component
        } catch (error) {
            console.error('Error updating tag data:', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setTagData(prevData => ({ ...prevData, [name]: value }));
    };



    return (
        <React.Fragment>

        <PopupState variant="popover" popupId="demo-popup-menu">
        {(popupState) => (
          <React.Fragment>
            <IconButton aria-label="option" 
                        {...bindTrigger(popupState)}
                        sx={{
                            borderRadius: 1, 
                            width: 40, height: 40,      
                        }}>
                <MoreHorizIcon />
            </IconButton>
            <Menu {...bindMenu(popupState)}>
                {/* <MenuItem onClick={() => handleEdit(id)}>Edit</MenuItem> */}
                <MenuItem onClick={() => handleDelete(id)}>Delete</MenuItem>
                <MenuItem onClick={() => handleHide(id)}>
                    {status === 'hide' ? 'Unhide' : 'Hide'}
                </MenuItem>
                {auth && (<MenuItem onClick={() => handleEdit(id)}>Edit</MenuItem>)}
                </Menu>
          </React.Fragment>
        )}
        </PopupState>

        <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Edit Tag</DialogTitle>
                <DialogContent>
                    {loading ? (
                        <p>Loading...</p>
                    ) : (
                        <React.Fragment>
                            <TextField
                                autoFocus
                                margin="dense"
                                name="tag"
                                label="Tag"
                                type="text"
                                fullWidth
                                value={tagData.tag}
                                onChange={handleChange}
                            />
                            <TextField
                                margin="dense"
                                name="remark"
                                label="Remark"
                                type="text"
                                fullWidth
                                value={tagData.remark}
                                onChange={handleChange}
                            />
                        </React.Fragment>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handleSave} color="primary">
                        Save
                    </Button>
                </DialogActions>
            </Dialog>


        </React.Fragment>

    );
};

export default MoreOption;
