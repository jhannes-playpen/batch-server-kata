const path = require('path')

module.exports = {
    entry: './src/index.jsx',
    devtool: "source-maps",
    output: {
        path: path.join(__dirname, '../../../target/classes/webapp'),
        filename: 'index.bundle.js'
    },

    module: {
        rules   : [
            {
                test: /\.jsx?/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                options: {
                    presets: ['react', 'es2015'],
                    plugins: ["transform-class-properties"]
                }
            },
            {
                test: /\.css$/,
                loaders: ['style-loader', 'css-loader']
            },
            {
                test: /\.(jpe?g|png|gif|svg)$/i, 
                loader: "file-loader?name=/public/icons/[name].[ext]"
            }
        ]
    }
};