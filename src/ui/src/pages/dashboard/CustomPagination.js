import { withStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableFooter from "@material-ui/core/TableFooter";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import PropTypes from "prop-types";
import React from "react";

const styles = theme => ({
  root: {
    width: "100%",
    marginTop: theme.spacing.unit * 3
  },
  tableWrapper: {
    overflowX: "auto"
  }
});

class CustomPagination extends React.Component {
  state = {};

  render() {
    const { classes, count, rowsPerPage, page } = this.props;

    return (
      <Table className={classes.table}>
        <TableFooter>
          <TableRow>
            <TablePagination
              count={count}
              rowsPerPage={rowsPerPage}
              page={page}
              labelRowsPerPage="Sitters per page"
              onChangePage={this.props.handleChangePage}
              onChangeRowsPerPage={this.props.handleChangeRowsPerPage}
            />
          </TableRow>
        </TableFooter>
      </Table>
    );
  }
}

CustomPagination.propTypes = {
  classes: PropTypes.object.isRequired,
  count: PropTypes.number,
  rowsPerPage: PropTypes.number,
  page: PropTypes.number,
  totalPages: PropTypes.number,
  handleChangePage: PropTypes.func,
  handleChangeRowsPerPage: PropTypes.func
};

export default withStyles(styles)(CustomPagination);
