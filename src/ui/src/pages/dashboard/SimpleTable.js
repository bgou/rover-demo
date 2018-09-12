import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import Axios from 'axios';

const styles = {
  root: {
    width: '100%',
    overflowX: 'auto',
  },
  table: {
    minWidth: 700,
  },
};

class SimpleTable extends React.Component {
  state = {
    open: false,
    data: []
  };

  async componentDidMount() {
    const response = await Axios.get('http://localhost:8080/api/sitters');
    console.log(response.data._embedded.sitters);
    this.setState({data: response.data._embedded.sitters});
    // data = response.data._embedded.sitters;
  }

  round(val) {
    return Number(val).toFixed(1);
  }

  render() {
    const { classes } = this.props;

    return (
      <Paper className={classes.root}>
        <Table className={classes.table}>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Photo</TableCell>
              <TableCell numeric>Rating</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {this.state.data.map(n => {
              return (
                <TableRow key={n.email}>
                  <TableCell component="th"
                             scope="row">
                    {n.name}
                  </TableCell>
                  <TableCell>{n.image}</TableCell>
                  <TableCell numeric>{this.round(n.rating)}</TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </Paper>
    );
  }
}

SimpleTable.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SimpleTable);
