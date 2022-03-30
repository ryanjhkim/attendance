import React, { Component } from 'react'


class Loading extends React.Component {
    state = {
      loadings: [],
    };
  
    enterLoading = index => {
      this.setState(({ loadings }) => {
        const newLoadings = [...loadings];
        newLoadings[index] = true;
  
        return {
          loadings: newLoadings,
        };
      });
      setTimeout(() => {
        this.setState(({ loadings }) => {
          const newLoadings = [...loadings];
          newLoadings[index] = false;
  
          return {
            loadings: newLoadings,
          };
        });
      }, 6000);

    };
}

export default Loading
  